package org.programmers.signalbuddy.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.dto.MemberJoinRequest;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private final Long id = 1L;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder().memberId(id).email("test@example.com").password("password123")
            .nickname("TestUser").profileImageUrl("http://example.com/profile.jpg").role(MemberRole.USER)
            .memberStatus(MemberStatus.ACTIVITY).build();
    }

    @Test
    @DisplayName("계정 조회 테스트")
    void getMember() {
        MemberResponse expectedResponse = MemberResponse.builder().memberId(id)
            .email("test@example.com").nickname("TestUser")
            .profileImageUrl("http://example.com/profile.jpg").role(MemberRole.USER)
            .memberStatus(MemberStatus.ACTIVITY).build();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        MemberResponse actualResponse = memberService.getMember(id);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(memberRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("계정 수정 테스트")
    void updateMember() {
        final MemberUpdateRequest request = MemberUpdateRequest.builder().email("test2@example.com")
            .nickname("TestUser2").profileImageUrl("http://example.com/profile.jpg")
            .password("password123").build();
        final MemberResponse expectedResponse = MemberResponse.builder().memberId(id)
            .email("test2@example.com").nickname("TestUser2")
            .profileImageUrl("http://example.com/profile.jpg").memberStatus(MemberStatus.ACTIVITY)
            .role(MemberRole.USER).build();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        final MemberResponse actualResponse = memberService.updateMember(id, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(memberRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("계정 탈퇴 테스트")
    void deleteMember() {
        final MemberStatus expected = MemberStatus.WITHDRAWAL;
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        final MemberStatus actual = memberService.deleteMember(id).getMemberStatus();
        assertThat(actual).isEqualTo(expected);
        verify(memberRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("회원 가입 성공")
    void savedMember() {

        MockMultipartFile profileImage = new MockMultipartFile(
            "profileImage",
            "",
            "image/jpeg",
            new byte[0]
        );

        //given
        final MemberJoinRequest request = MemberJoinRequest.builder()
            .email("test2@example.com")
            .nickname("TestUser2")
            .password("password123")
            .profileImageUrl(profileImage)
            .build();
        final Member expectedMember = Member.builder()
            .memberId(id)
            .email("test2@example.com")
            .nickname("TestUser2")
            .profileImageUrl("none")
            .memberStatus(MemberStatus.ACTIVITY)
            .role(MemberRole.USER).build();

        when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);

        //when
        MemberResponse actualResponse = memberService.joinMember(request);

        //then
        assertThat(actualResponse.getEmail()).isEqualTo(expectedMember.getEmail());
        assertThat(actualResponse.getNickname()).isEqualTo(expectedMember.getNickname());
        assertThat(actualResponse.getProfileImageUrl()).isEqualTo(expectedMember.getProfileImageUrl());
        assertThat(actualResponse.getMemberStatus()).isEqualTo(expectedMember.getMemberStatus());
        assertThat(actualResponse.getRole()).isEqualTo(expectedMember.getRole());

        verify(memberRepository, times(1)).save(any(Member.class));
    }

}