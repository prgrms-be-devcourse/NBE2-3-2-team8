package org.programmers.signalbuddy.domain.feedback.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.RepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

class FeedbackRepositoryTest extends RepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setup() {
        member = Member.builder().email("test@test.com").password("123456").role(MemberRole.USER)
            .nickname("tester").memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://test-image.com/test-123131").build();
        member = memberRepository.save(member);

        List<Feedback> feedbackList = new ArrayList<>();
        for (int i = 0; i < 123; i++) {
            String subject = "test subject";
            String content = "test content";

            Feedback feedback = Feedback.create()
                .subject(subject).content(content).member(member)
                .build();

            feedbackList.add(feedback);
        }
        feedbackRepository.saveAll(feedbackList);
    }

    @DisplayName("탈퇴하지 않은 유저들의 피드백 목록 가져오기")
    @Test
    void findAllByActiveMembers() {
        // when
        Pageable pageable = PageRequest.of(3, 10);
        Long answerStatus = -1L; // 모든 피드백 보기
        Page<FeedbackResponse> actual = feedbackRepository.findAllByActiveMembers(pageable, answerStatus);

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

    // TODO: 더 다양한 데이터를 이용하여 추가 검증 필요
    @DisplayName("관리자 : 피드백 목록을 다양한 조건으로 조회")
    @Test
    void findAll() {
        // when
        Pageable pageable = PageRequest.of(3, 10, Direction.DESC, "feedbackId", "createdAt");
        Page<FeedbackResponse> actual = feedbackRepository.findAll(pageable, null, null,
            0L);

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

    @DisplayName("관리자 : 피드백 목록을 다양한 조건으로 조회, 잘못된 컬럼명을 요청할 때")
    @Test
    void findAllFailure() {
        // given
        Pageable pageable = PageRequest.of(3, 10, Direction.DESC, "xxxxx");

        // when & then
        assertThatThrownBy(() -> feedbackRepository.findAll(pageable, null, LocalDate.now(),
            0L)).isExactlyInstanceOf(InvalidDataAccessApiUsageException.class);
    }
}