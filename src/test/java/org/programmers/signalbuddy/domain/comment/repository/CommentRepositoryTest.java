package org.programmers.signalbuddy.domain.comment.repository;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.dto.CommentResponse;
import org.programmers.signalbuddy.domain.comment.entity.Comment;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.RepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class CommentRepositoryTest extends RepositoryTest {

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
        member = Member.builder().email("test@test.com").password("123456").role("USER")
            .nickname("tester").memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://test-image.com/test-123131").build();
        member = memberRepository.save(member);

        String subject = "test subject";
        String content = "test content";
        FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
        feedback = feedbackRepository.save(Feedback.create(request, member));

        List<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Comment comment = Comment.creator()
                .request(new CommentRequest(feedback.getFeedbackId(), "test comment content"))
                .feedback(feedback).member(member).build();
            commentList.add(comment);
        }
        commentRepository.saveAll(commentList);
    }

    @DisplayName("특정 피드백의 댓글 목록 가져오기")
    @Test
    void findAllByFeedbackIdAndActiveMembers() {
        // given
        Long feedbackId = feedback.getFeedbackId();

        // when
        Pageable pageable = PageRequest.of(2, 7);
        Page<CommentResponse> actual = commentRepository.findAllByFeedbackIdAndActiveMembers(
            feedbackId, pageable);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getTotalElements()).isEqualTo(30);
            softAssertions.assertThat(actual.getTotalPages()).isEqualTo(5);
            softAssertions.assertThat(actual.getNumber()).isEqualTo(2);
            softAssertions.assertThat(actual.getContent().size()).isEqualTo(7);
            softAssertions.assertThat(actual.getContent().get(3).getCommentId()).isNotNull();
            softAssertions.assertThat(actual.getContent().get(3).getContent())
                .isEqualTo("test comment content");
            softAssertions.assertThat(actual.getContent().get(3).getMember().getMemberId())
                .isEqualTo(member.getMemberId());
        });
    }
}