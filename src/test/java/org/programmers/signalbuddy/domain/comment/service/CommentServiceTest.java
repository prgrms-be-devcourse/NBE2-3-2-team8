package org.programmers.signalbuddy.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.entity.Comment;
import org.programmers.signalbuddy.domain.comment.exception.CommentErrorCode;
import org.programmers.signalbuddy.domain.comment.repository.CommentRepository;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@TestMethodOrder(OrderAnnotation.class)
class CommentServiceTest extends ServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    private Member member;
    private Feedback feedback;
    private Comment comment;

    @BeforeEach
    void setup() {
        member = Member.builder().email("test@test.com").password("123456").role("USER")
            .nickname("tester").memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://test-image.com/test-123131").build();
        member = memberRepository.save(member);

        String subject = "test subject";
        String content = "test content";
        FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
        feedback = feedbackRepository.save(Feedback.create(request, member));

        comment = Comment.creator()
            .request(new CommentRequest(feedback.getFeedbackId(), "test comment content"))
            .feedback(feedback).member(member).build();
        comment = commentRepository.save(comment);
    }

    @DisplayName("댓글 작성 성공")
    @Test
    @Order(1)
    void writeComment() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        String content = "test comment content";
        CommentRequest request = new CommentRequest(feedbackId, content);
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName(member.getMemberId().toString());

        // when
        commentService.writeComment(request, user);

        // then
        Optional<Comment> actual = commentRepository.findById(2L);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).get().isNotNull();
            softAssertions.assertThat(actual.get().getCommentId()).isNotNull();
            softAssertions.assertThat(actual.get().getContent()).isEqualTo(content);
            softAssertions.assertThat(actual.get().getMember().getMemberId())
                .isEqualTo(Long.parseLong(user.getName()));
            softAssertions.assertThat(actual.get().getFeedback().getFeedbackId())
                .isEqualTo(feedbackId);
        });
    }

    @DisplayName("댓글 수정 성공")
    @Test
    @Order(2)
    void updateComment() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        String updatedContent = "update comment content";
        CommentRequest request = new CommentRequest(feedbackId, updatedContent);
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName(member.getMemberId().toString());

        // when
        commentService.updateComment(comment.getCommentId(), request, user);

        // then
        Optional<Comment> actual = commentRepository.findById(comment.getCommentId());
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get().getCommentId()).isNotNull();
            softAssertions.assertThat(actual.get().getContent()).isEqualTo(updatedContent);
            softAssertions.assertThat(actual.get().getMember().getMemberId())
                .isEqualTo(Long.parseLong(user.getName()));
        });
    }

    @DisplayName("댓글 작성자와 다른 사람이 수정 시, 실패")
    @Test
    @Order(3)
    void updateCommentFailure() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        String updatedContent = "update comment content";
        CommentRequest request = new CommentRequest(feedbackId, updatedContent);
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName("10");

        // when & then
        assertThatThrownBy(() -> {
            commentService.updateComment(comment.getCommentId(), request, user);
        }).isExactlyInstanceOf(BusinessException.class)
            .hasMessage(CommentErrorCode.COMMENT_MODIFIER_NOT_AUTHORIZED.getMessage());
    }

    @DisplayName("댓글 삭제 성공")
    @Test
    @Order(4)
    void deleteComment() {
        // given
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName(member.getMemberId().toString());

        // when
        commentService.deleteComment(comment.getCommentId(), user);

        // then
        assertThat(commentRepository.existsById(comment.getCommentId())).isFalse();
    }

    @DisplayName("댓글 작성자와 다른 사람이 삭제 시, 실패")
    @Test
    @Order(5)
    void deleteCommentFailure() {
        // given
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName("10");

        // when & then
        assertThatThrownBy(() -> {
            commentService.deleteComment(comment.getCommentId(), user);
        }).isExactlyInstanceOf(BusinessException.class)
            .hasMessage(CommentErrorCode.COMMENT_ELIMINATOR_NOT_AUTHORIZED.getMessage());
    }
}