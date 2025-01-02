package org.programmers.signalbuddy.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.exception.FeedbackErrorCode;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.like.entity.Like;
import org.programmers.signalbuddy.domain.like.repository.LikeRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.exception.BusinessException;
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
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new BusinessException(FeedbackErrorCode.NOT_FOUND_FEEDBACK));
        Member member = memberRepository.findById(user.getMemberId())
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));

        likeRepository.save(Like.create(member, feedback));
        feedback.increaseLike();
    }
}
