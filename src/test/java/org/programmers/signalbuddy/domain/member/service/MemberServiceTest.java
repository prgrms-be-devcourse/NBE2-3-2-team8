package org.programmers.signalbuddy.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.MemberStatus;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;

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
            .nickname("TestUser").profileImageUrl("http://example.com/profile.jpg").role("USER")
            .memberStatus(MemberStatus.ACTIVITY).build();
    }

    @Test
    void getMember() {
        MemberResponse expectedResponse = MemberResponse.builder().memberId(id)
            .email("test@example.com").nickname("TestUser")
            .profileImageUrl("http://example.com/profile.jpg").role("USER")
            .memberStatus(MemberStatus.ACTIVITY).build();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        MemberResponse actualResponse = memberService.getMember(id);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(memberRepository, times(1)).findById(id);
    }

    @Test
    void updateMember() {
        final MemberUpdateRequest request = MemberUpdateRequest.builder().email("test2@example.com")
            .nickname("TestUser2").profileImageUrl("http://example.com/profile.jpg")
            .password("password123").build();
        final MemberResponse expectedResponse = MemberResponse.builder().memberId(id)
            .email("test2@example.com").nickname("TestUser2")
            .profileImageUrl("http://example.com/profile.jpg").memberStatus(MemberStatus.ACTIVITY)
            .role("USER").build();

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        final MemberResponse actualResponse = memberService.updateMember(id, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(memberRepository, times(1)).findById(id);
    }
}