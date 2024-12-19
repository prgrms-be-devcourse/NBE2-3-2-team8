package org.programmers.signalbuddy.domain.comment.service;

import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.entity.Comment;
import org.programmers.signalbuddy.domain.comment.repository.CommentRepository;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    @BeforeEach
    void setup() {
        member = Member.builder()
            .email("test@test.com")
            .password("123456")
            .role("USER")
            .nickname("tester")
            .memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://test-image.com/test-123131")
            .build();
        member = memberRepository.save(member);

        String subject = "test subject";
        String content = "test content";
        FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
        feedback = feedbackRepository.save(Feedback.create(request, member));
    }

    @DisplayName("댓글 작성 성공")
    @Test
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
        Optional<Comment> actual = commentRepository.findById(1L);
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
}