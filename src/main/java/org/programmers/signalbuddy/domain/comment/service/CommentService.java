package org.programmers.signalbuddy.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.entity.Comment;
import org.programmers.signalbuddy.domain.comment.exception.CommentErrorCode;
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

    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
    @Transactional
    public void updateComment(Long commentId, CommentRequest request, User user) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND_COMMENT));

        // 댓글 작성자와 수정 요청자가 다른 경우
        if (!user.getName().equals(comment.getMember().getMemberId().toString())) {
            throw new BusinessException(CommentErrorCode.COMMENT_MODIFIER_NOT_AUTHORIZED);
        }

        comment.updateContent(request);
    }

    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException(CommentErrorCode.NOT_FOUND_COMMENT));

        // 댓글 작성자와 삭제 요청자가 다른 경우
        if (!user.getName().equals(comment.getMember().getMemberId().toString())) {
            throw new BusinessException(CommentErrorCode.COMMENT_ELIMINATOR_NOT_AUTHORIZED);
        }

        commentRepository.deleteById(commentId);
    }
}
