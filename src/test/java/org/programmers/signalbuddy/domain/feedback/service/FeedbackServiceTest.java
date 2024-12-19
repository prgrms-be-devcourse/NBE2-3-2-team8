package org.programmers.signalbuddy.domain.feedback.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.member.MemberStatus;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class FeedbackServiceTest extends ServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        Member member = Member.builder()
            .email("test@test.com")
            .password("123456")
            .role("USER")
            .nickname("tester")
            .memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://test-image.com/test-123131")
            .build();

        memberRepository.save(member);
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
        user.setName("1");

        // when
        FeedbackResponse actual = feedbackService.writeFeedback(request, user);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getFeedbackId()).isEqualTo(1L);
            softAssertions.assertThat(actual.getSubject()).isEqualTo(subject);
            softAssertions.assertThat(actual.getContent()).isEqualTo(content);
            softAssertions.assertThat(actual.getLikeCount()).isZero();
            softAssertions.assertThat(actual.getMember().getMemberId())
                .isEqualTo(Long.parseLong(user.getName()));
        });
    }
}