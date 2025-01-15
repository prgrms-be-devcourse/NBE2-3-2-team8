package org.programmers.signalbuddy.domain.like.batch;

import static org.programmers.signalbuddy.domain.like.service.LikeService.generateKey;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.like.dto.LikeRequestType;
import org.programmers.signalbuddy.domain.like.dto.LikeUpdateRequest;
import org.programmers.signalbuddy.domain.like.repository.LikeJdbcRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class RequestLikeWriter implements ItemWriter<LikeUpdateRequest> {

    private final FeedbackRepository feedbackRepository;
    private final LikeJdbcRepository likeJdbcRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    @Override
    public void write(Chunk<? extends LikeUpdateRequest> chunk) {
        log.info("like job chunk size : {}", chunk.size());

        List<LikeUpdateRequest> savedLikeList = new ArrayList<>();   // 저장할 좋아요 데이터
        List<LikeUpdateRequest> deletedLikeList = new ArrayList<>(); // 삭제할 좋아요 데이터
        List<String> likeKeyList = new ArrayList<>(); // Redis에 저장된 좋아요 데이터의 키

        // 요청된 좋아요 개수 및 좋아요 데이터 반영
        for (LikeUpdateRequest request : chunk.getItems()) {

            Feedback feedback = feedbackRepository.findById(request.getFeedbackId()).orElse(null);
            if (feedback == null) {
                redisTemplate.delete(generateKey(request));
                continue;
            }

            if (LikeRequestType.ADD.equals(request.getLikeRequestType())) {
                savedLikeList.add(request);
                feedback.increaseLike();

            } else if (LikeRequestType.CANCEL.equals(request.getLikeRequestType())) {
                deletedLikeList.add(request);
                feedback.decreaseLike();
            }

            likeKeyList.add(generateKey(request));
        }

        likeJdbcRepository.saveAllInBatch(savedLikeList);
        likeJdbcRepository.deleteAllByLikeRequestsInBatch(deletedLikeList);
        redisTemplate.delete(likeKeyList);
    }
}
