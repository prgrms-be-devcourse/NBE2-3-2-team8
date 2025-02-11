package org.programmers.signalbuddy.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.dto.CommentResponse;
import org.programmers.signalbuddy.domain.comment.entity.Comment;
import org.programmers.signalbuddy.domain.comment.exception.CommentErrorCode;
import org.programmers.signalbuddy.domain.comment.repository.CommentRepository;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.dto.PageResponse;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public void writeComment(CommentRequest request, CustomUser2Member user) {
        Member member = memberRepository.findByIdOrThrow(user.getMemberId());
        Feedback feedback = feedbackRepository.findByIdOrThrow(request.getFeedbackId());

        Comment comment = Comment.create()
            .content(request.getContent()).feedback(feedback).member(member)
            .build();

        // 관리자일 때 피드백 상태 변경
        if (Member.isAdmin(comment.getMember())) {
            feedback.updateFeedbackStatus();
        }

        commentRepository.save(comment);
    }

    public PageResponse<CommentResponse> searchCommentList(Long feedbackId, Pageable pageable) {
        Page<CommentResponse> responsePage = commentRepository.findAllByFeedbackIdAndActiveMembers(
            feedbackId, pageable);
        return new PageResponse<>(responsePage);
    }

    @Transactional
    public void updateComment(Long commentId, CommentRequest request, CustomUser2Member user) {
        Comment comment = commentRepository.findByIdOrThrow(commentId);

        // 수정 요청자와 댓글 작성자 다른 경우
        if (Member.isNotSameMember(user, comment.getMember())) {
            throw new BusinessException(CommentErrorCode.COMMENT_MODIFIER_NOT_AUTHORIZED);
        }

        comment.updateContent(request);
    }

    @Transactional
    public void deleteComment(Long commentId, CustomUser2Member user) {
        Comment comment = commentRepository.findByIdOrThrow(commentId);

        // 삭제 요청자와 댓글 작성자 다른 경우
        if (Member.isNotSameMember(user, comment.getMember())) {
            throw new BusinessException(CommentErrorCode.COMMENT_ELIMINATOR_NOT_AUTHORIZED);
        }

        // 관리자일 때 피드백 상태 변경
        if (Member.isAdmin(comment.getMember())) {
            comment.getFeedback().updateFeedbackStatus();
        }

        commentRepository.deleteById(commentId);
    }
}
