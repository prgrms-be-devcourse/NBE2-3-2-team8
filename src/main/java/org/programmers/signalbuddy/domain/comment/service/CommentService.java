package org.programmers.signalbuddy.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.entity.Comment;
import org.programmers.signalbuddy.domain.comment.repository.CommentRepository;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.exception.FeedbackErrorCode;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
    @Transactional
    public void writeComment(CommentRequest request, User user) {
        Member memeber = memberRepository.findById(Long.parseLong(user.getName()))
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
        Feedback feedback = feedbackRepository.findById(request.getFeedbackId())
            .orElseThrow(() -> new BusinessException(FeedbackErrorCode.NOT_FOUND_FEEDBACK));

        Comment comment = Comment.creator()
            .request(request)
            .feedback(feedback)
            .member(memeber)
            .build();

        commentRepository.save(comment);
    }
}
