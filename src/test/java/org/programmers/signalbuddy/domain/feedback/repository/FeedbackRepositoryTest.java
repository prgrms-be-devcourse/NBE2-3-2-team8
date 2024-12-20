package org.programmers.signalbuddy.domain.feedback.repository;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.RepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class FeedbackRepositoryTest extends RepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setup() {
        member = Member.builder().email("test@test.com").password("123456").role("USER")
            .nickname("tester").memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://test-image.com/test-123131").build();
        member = memberRepository.save(member);

        List<Feedback> feedbackList = new ArrayList<>();
        for (int i = 0; i < 123; i++) {
            String subject = "test subject";
            String content = "test content";
            FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
            feedbackList.add(Feedback.create(request, member));
        }
        feedbackRepository.saveAll(feedbackList);
    }

    @DisplayName("탈퇴하지 않은 유저들의 피드백 목록 가져오기")
    @Test
    void findAllByActiveMembers() {
        // when
        Pageable pageable = PageRequest.of(3, 10);
        Page<FeedbackResponse> actual = feedbackRepository.findAllByActiveMembers(pageable);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getTotalElements()).isEqualTo(123);
            softAssertions.assertThat(actual.getTotalPages()).isEqualTo(13);
            softAssertions.assertThat(actual.getNumber()).isEqualTo(3);
            softAssertions.assertThat(actual.getContent().size()).isEqualTo(10);
            softAssertions.assertThat(actual.getContent().get(3).getFeedbackId()).isNotNull();
            softAssertions.assertThat(actual.getContent().get(3).getMember().getMemberId())
                .isEqualTo(member.getMemberId());
        });
    }
}