package org.programmers.signalbuddy.domain.feedback.repository;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.member.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FeedbackJdbcRepositoryTest {

    @Autowired
    private FeedbackJdbcRepository feedbackJdbcRepository;

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
            FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
            feedbackList.add(Feedback.create(request, member));
        }
        feedbackRepository.saveAll(feedbackList);
    }

    @AfterEach
    void tearDown() {
        feedbackRepository.deleteAll();
    }

    // TODO: H2를 MariaDB TestContainers로 바꾸면 테스트 코드 완성하기
    @DisplayName("Full Text Search를 이용한 검색 쿼리")
    @Test
    void fullTextSearch() {
//        // when
//        Pageable pageable = PageRequest.of(3, 10);
//        Page<FeedbackResponse> actual = feedbackJdbcRepository.fullTextSearch(pageable, "test", 0L);
//
//        // then
//        SoftAssertions.assertSoftly(softAssertions -> {
//            softAssertions.assertThat(actual.getTotalElements()).isEqualTo(123);
//            softAssertions.assertThat(actual.getTotalPages()).isEqualTo(13);
//            softAssertions.assertThat(actual.getNumber()).isEqualTo(3);
//            softAssertions.assertThat(actual.getContent().size()).isEqualTo(10);
//            softAssertions.assertThat(actual.getContent().get(3).getFeedbackId()).isNotNull();
//            softAssertions.assertThat(actual.getContent().get(3).getMember().getMemberId())
//                .isEqualTo(member.getMemberId());
//        });
    }
}