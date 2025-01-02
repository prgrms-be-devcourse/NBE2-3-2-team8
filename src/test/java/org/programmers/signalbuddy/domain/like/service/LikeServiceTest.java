package org.programmers.signalbuddy.domain.like.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.like.dto.LikeExistResponse;
import org.programmers.signalbuddy.domain.like.entity.Like;
import org.programmers.signalbuddy.domain.like.repository.LikeRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.security.CustomUserDetails;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class LikeServiceTest extends ServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeRepository likeRepository;

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

    @DisplayName("좋아요 추가")
    @Test
    void addLike() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        CustomUser2Member user = new CustomUser2Member(
            new CustomUserDetails(member.getMemberId(), "", "",
                "", "", MemberRole.USER, MemberStatus.ACTIVITY));

        // when
        likeService.addLike(feedbackId, user);

        // then
        List<Like> actual = likeRepository.findAll();
        Optional<Feedback> updatedFeedback = feedbackRepository.findById(feedbackId);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isNotEmpty();
            softAssertions.assertThat(actual.get(0).getLikeId()).isNotNull();
            softAssertions.assertThat(updatedFeedback).isPresent();
            softAssertions.assertThat(updatedFeedback.get().getLikeCount()).isOne();
        });
    }

    @DisplayName("좋아요 취소")
    @Test
    void deleteLike() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        CustomUser2Member user = new CustomUser2Member(
            new CustomUserDetails(member.getMemberId(), "", "",
                "", "", MemberRole.USER, MemberStatus.ACTIVITY));

        // when
        likeService.addLike(feedbackId, user);
        likeService.deleteLike(feedbackId, user);

        // then
        List<Like> actual = likeRepository.findAll();
        Optional<Feedback> updatedFeedback = feedbackRepository.findById(feedbackId);
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isEmpty();
            softAssertions.assertThat(updatedFeedback).isPresent();
            softAssertions.assertThat(updatedFeedback.get().getLikeCount()).isZero();
        });
    }

    @DisplayName("해당 좋아요가 존재할 때")
    @Test
    void existsLikeTrue() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        CustomUser2Member user = new CustomUser2Member(
            new CustomUserDetails(member.getMemberId(), "", "",
                "", "", MemberRole.USER, MemberStatus.ACTIVITY));

        // when
        likeService.addLike(feedbackId, user);
        LikeExistResponse actual = likeService.existsLike(feedbackId, user);

        // then
        assertThat(actual.getStatus()).isTrue();
    }

    @DisplayName("해당 좋아요가 존재하지 않을 때")
    @Test
    void existsLikeFalse() {
        // given
        Long feedbackId = feedback.getFeedbackId();
        CustomUser2Member user = new CustomUser2Member(
            new CustomUserDetails(member.getMemberId(), "", "",
                "", "", MemberRole.USER, MemberStatus.ACTIVITY));

        // when
        LikeExistResponse actual = likeService.existsLike(feedbackId, user);

        // then
        assertThat(actual.getStatus()).isFalse();
    }
}