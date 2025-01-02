package org.programmers.signalbuddy.domain.feedback.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.exception.FeedbackErrorCode;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class FeedbackServiceTest extends ServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Feedback feedback;

    @BeforeEach
    void setup() {
        member = Member.builder()
            .email("test@test.com")
            .password("123456")
            .role(MemberRole.USER)
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

    @DisplayName("피드백 작성 성공")
    @Test
    void writeFeedback() {
        // given
        String subject = "test subject";
        String content = "test content";
        FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName(member.getMemberId().toString());

        // when
        FeedbackResponse actual = feedbackService.writeFeedback(request, user);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getFeedbackId()).isNotNull();
            softAssertions.assertThat(actual.getSubject()).isEqualTo(subject);
            softAssertions.assertThat(actual.getContent()).isEqualTo(content);
            softAssertions.assertThat(actual.getLikeCount()).isZero();
            softAssertions.assertThat(actual.getMember().getMemberId())
                .isEqualTo(Long.parseLong(user.getName()));
        });
    }

    @DisplayName("피드백 수정 성공")
    @Test
    void updateFeedback() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        String updatedSubject = "test updated subject";
        String updatedContent = "test updated content";
        FeedbackWriteRequest request = new FeedbackWriteRequest(updatedSubject, updatedContent);
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName(member.getMemberId().toString());

        // when
        feedbackService.updateFeedback(feedbackId, request, user);

        // then
        Optional<Feedback> actual = feedbackRepository.findById(feedbackId);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get().getFeedbackId()).isEqualTo(feedbackId);
            softAssertions.assertThat(actual.get().getSubject()).isEqualTo(updatedSubject);
            softAssertions.assertThat(actual.get().getContent()).isEqualTo(updatedContent);
            softAssertions.assertThat(actual.get().getMember().getMemberId())
                .isEqualTo(Long.parseLong(user.getName()));
        });
    }

    @DisplayName("피드백 작성자와 다른 사람이 수정 시 실패")
    @Test
    void updateFeedbackFailure() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        String updatedSubject = "test updated subject";
        String updatedContent = "test updated content";
        FeedbackWriteRequest request = new FeedbackWriteRequest(updatedSubject, updatedContent);
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName("99999");

        // when & then
        assertThatThrownBy(() -> {
            feedbackService.updateFeedback(feedbackId, request, user);
        }).isExactlyInstanceOf(BusinessException.class)
            .hasMessage(FeedbackErrorCode.FEEDBACK_MODIFIER_NOT_AUTHORIZED.getMessage());
    }

    @DisplayName("피드백 삭제 성공")
    @Test
    void deleteFeedback() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName(member.getMemberId().toString());

        // when
        feedbackService.deleteFeedback(feedbackId, user);

        // then
        assertThat(feedbackRepository.existsById(feedbackId)).isFalse();
    }

    @DisplayName("피드백 작성자와 다른 사람이 삭제 시 실패")
    @Test
    void deleteFeedbackFailure() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        // TODO: User 객체는 나중에 변경해야 함!
        User user = new User();
        user.setName("99999");

        // when & then
        assertThatThrownBy(() -> {
            feedbackService.deleteFeedback(feedbackId, user);
        }).isExactlyInstanceOf(BusinessException.class)
            .hasMessage(FeedbackErrorCode.FEEDBACK_MODIFIER_NOT_AUTHORIZED.getMessage());
    }

    @DisplayName("피드백 상세 조회 성공")
    @Test
    void searchFeedbackDetail() {
        // given
        Long feedbackId = feedback.getFeedbackId();

        // when
        FeedbackResponse response = feedbackService.searchFeedbackDetail(feedbackId);

        // then
        Optional<Feedback> actual = feedbackRepository.findById(feedbackId);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get().getFeedbackId()).isEqualTo(feedbackId);
            softAssertions.assertThat(actual.get().getSubject()).isEqualTo(response.getSubject());
            softAssertions.assertThat(actual.get().getContent()).isEqualTo(response.getContent());
            softAssertions.assertThat(actual.get().getMember().getMemberId())
                .isEqualTo(feedback.getMember().getMemberId());
        });
    }

    @DisplayName("Member id로 피드백 목록 조회")
    @Test
    void searchFeedbackDetailFailure() {
        for (int i = 1; i <= 10; i++) {
            String subject = "test subject " + i;
            String content = "test content " + i;
            FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
            feedbackRepository.save(Feedback.create(request, member));
        }

        final Page<FeedbackResponse> feedbacks = feedbackService.findPagedFeedbacksByMember(
            member.getMemberId(), PageRequest.of(0, 5));

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(feedbacks.getTotalPages()).isEqualTo(3);
            softAssertions.assertThat(feedbacks.getTotalElements()).isEqualTo(11);
            softAssertions.assertThat(feedbacks.getContent().size()).isEqualTo(5); // 한 페이지당 5개

            FeedbackResponse firstFeedback = feedbacks.getContent().get(0);
            softAssertions.assertThat(firstFeedback.getSubject()).isEqualTo("test subject 10");
            softAssertions.assertThat(firstFeedback.getContent()).isEqualTo("test content 10");
        });
    }
}