package org.programmers.signalbuddy.domain.like.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.like.dto.LikeExistResponse;
import org.programmers.signalbuddy.domain.like.dto.LikeRequestType;
import org.programmers.signalbuddy.domain.like.dto.LikeUpdateRequest;
import org.programmers.signalbuddy.domain.like.exception.LikeErrorCode;
import org.programmers.signalbuddy.domain.like.repository.LikeRepository;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String LIKE_KEY_PREFIX = "like:";

    @Transactional
    public void addLike(Long feedbackId, CustomUser2Member user) {
        String key = generateKey(feedbackId, user.getMemberId());

        // 삭제 요청 데이터가 Redis에 있을 때
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
            return;
        }

        boolean isExisted = likeRepository.existsByMemberAndFeedback(user.getMemberId(), feedbackId);
        if (isExisted) {
            throw new BusinessException(LikeErrorCode.ALREADY_ADDED_LIKE);
        }

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, LikeRequestType.ADD.name(), 3L, TimeUnit.MINUTES);
    }

    public LikeExistResponse existsLike(Long feedbackId, CustomUser2Member user) {
        boolean isExisted = likeRepository.existsByMemberAndFeedback(user.getMemberId(), feedbackId);
        return new LikeExistResponse(isExisted);
    }

    @Transactional
    public void deleteLike(Long feedbackId, CustomUser2Member user) {
        String key = generateKey(feedbackId, user.getMemberId());

        // 좋아요 데이터가 아직 DB에 저장되지 않은 경우 (Redis에만 있을 때)
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.delete(key);
            return;
        }

        boolean isExisted = likeRepository.existsByMemberAndFeedback(user.getMemberId(), feedbackId);
        if (!isExisted) {
            throw new BusinessException(LikeErrorCode.NOT_FOUND_LIKE);
        }

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, LikeRequestType.CANCEL.name(), 3L, TimeUnit.MINUTES);
    }

    String generateKey(Long feedbackId, Long memberId) {
        return LIKE_KEY_PREFIX + feedbackId + ":" + memberId;
    }

    public static String generateKey(LikeUpdateRequest request) {
        return LIKE_KEY_PREFIX + request.getFeedbackId() + ":" + request.getMemberId();
    }

    public static String getLikeKeyPrefix() {
        return LIKE_KEY_PREFIX;
    }
}
