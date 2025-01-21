package org.programmers.signalbuddy.domain.like.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.like.service.LikeService;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.db.RedisTestContainer;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.security.basic.CustomUserDetails;
import org.programmers.signalbuddy.global.support.BatchTest;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

class LikeJobConfigTest extends BatchTest implements RedisTestContainer {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job likeRequestJob;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    private List<Member> savedMemberList;

    @BeforeEach
    void setup() {
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Member member = Member.builder()
                .email("test@test.com")
                .password("123456" + i)
                .role(MemberRole.USER)
                .nickname("tester")
                .memberStatus(MemberStatus.ACTIVITY)
                .profileImageUrl("https://test-image.com/test-123131")
                .build();
            memberList.add(member);
        }
        savedMemberList = memberRepository.saveAll(memberList);
    }

    @DisplayName("동시에 많은 좋아요 추가/취소 요청이 발생할 때, 좋아요 개수의 정합성 확인")
    @Test
    void likeRequestJob() throws Exception {
        // given
        int addLikeThreadCount = savedMemberList.size(); // 좋아요 추가 요청의 스레드 개수
        ExecutorService executorService = Executors.newFixedThreadPool(addLikeThreadCount);    // 스레드 풀 생성
        final CountDownLatch latch = new CountDownLatch(addLikeThreadCount); // 스레드 대기 관리
        int deleteLikeThreadCount = (int) (addLikeThreadCount * 0.3);   // 좋아요 취소 요청의 스레드 개수
        final CountDownLatch latch2 = new CountDownLatch(deleteLikeThreadCount); // 스레드 대기 관리
        ExecutorService executorService2 = Executors.newFixedThreadPool(deleteLikeThreadCount);    // 스레드 풀 생성

        String subject = "test subject";
        String content = "test content";
        FeedbackWriteRequest request = new FeedbackWriteRequest(subject, content);
        Feedback savedFeedback = feedbackRepository.saveAndFlush(Feedback.create(request, savedMemberList.get(0)));

        // when
        // 좋아요 추가
        for (int i = 0; i < addLikeThreadCount; i++) {
            Member member = savedMemberList.get(i);
            executorService.execute(() -> {
                try {
                    CustomUser2Member user = new CustomUser2Member(
                        new CustomUserDetails(member.getMemberId(), "", "",
                            "", "", MemberRole.USER, MemberStatus.ACTIVITY));

                    likeService.addLike(savedFeedback.getFeedbackId(), user);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 스레드가 다 끝날 때까지 기다리기
        latch.await();
        executorService.shutdown();

        // 좋아요 취소
        for (int i = 0; i < deleteLikeThreadCount; i++) {
            Member member = savedMemberList.get(i);
            executorService2.execute(() -> {
                try {
                    CustomUser2Member user = new CustomUser2Member(
                        new CustomUserDetails(member.getMemberId(), "", "",
                            "", "", MemberRole.USER, MemberStatus.ACTIVITY));

                    likeService.deleteLike(savedFeedback.getFeedbackId(), user);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch2.countDown();
                }
            });
        }

        // 스레드가 다 끝날 때까지 기다리기
        latch2.await();
        executorService2.shutdown();

        jobLauncherTestUtils.setJob(likeRequestJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        Feedback updatedFeedback = feedbackRepository.findById(savedFeedback.getFeedbackId()).get();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
            softAssertions.assertThat(updatedFeedback.getLikeCount())
                .isEqualTo(addLikeThreadCount - deleteLikeThreadCount);
        });
    }
}