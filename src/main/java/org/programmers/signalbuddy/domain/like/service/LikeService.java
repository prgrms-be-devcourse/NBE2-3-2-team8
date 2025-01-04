package org.programmers.signalbuddy.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.like.dto.LikeExistResponse;
import org.programmers.signalbuddy.domain.like.entity.Like;
import org.programmers.signalbuddy.domain.like.repository.LikeRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void addLike(Long feedbackId, CustomUser2Member user) {
        Feedback feedback = feedbackRepository.findByIdOrThrow(feedbackId);
        Member member = memberRepository.findByIdOrThrow(user.getMemberId());

        likeRepository.save(Like.create(member, feedback));
        feedback.increaseLike();
    }

    public LikeExistResponse existsLike(Long feedbackId, CustomUser2Member user) {
        boolean isExisted = likeRepository.existsByMemberAndFeedback(user.getMemberId(), feedbackId);
        return new LikeExistResponse(isExisted);
    }

    @Transactional
    public void deleteLike(Long feedbackId, CustomUser2Member user) {
        Feedback feedback = feedbackRepository.findByIdOrThrow(feedbackId);
        Member member = memberRepository.findByIdOrThrow(user.getMemberId());

        likeRepository.deleteByMemberAndFeedback(member, feedback);
        feedback.decreaseLike();
    }
}
