package org.programmers.signalbuddy.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.programmers.signalbuddy.domain.member.dto.MemberJoinRequest;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private final Long id = 1L;
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AwsFileService awsFileService;

    @InjectMocks
    private MemberService memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder().memberId(id).email("test@example.com").password("password123")
            .nickname("TestUser").profileImageUrl("http://example.com/profile.jpg")
            .role(MemberRole.USER).memberStatus(MemberStatus.ACTIVITY).build();
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
        final MemberUpdateRequest updateRequest = MemberUpdateRequest.builder()
            .email("test2@example.com").nickname("TestUser2")
            .imageFile(mock(MockMultipartFile.class)).password("password123").build();

        final MemberResponse expectedResponse = MemberResponse.builder().memberId(id)
            .email("test2@example.com").nickname("TestUser2")
            .profileImageUrl("updated-image.jpg").memberStatus(MemberStatus.ACTIVITY)
            .role(MemberRole.USER).build();

        // MockHttpServletRequest와 MockHttpSession 생성
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpSession mockSession = new MockHttpSession();

        mockRequest.setSession(mockSession); // MockHttpSession 설정
        when(memberRepository.findById(id)).thenReturn(Optional.of(member));
        when(awsFileService.saveProfileImage(updateRequest.getImageFile())).thenReturn("updated-image.jpg");

        final MemberResponse actualResponse = memberService.updateMember(id, updateRequest,
            mockRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(memberRepository, times(1)).findById(id);

        // 세션에 값이 저장되었는지 확인
        Object sessionAttribute = mockSession.getAttribute("SPRING_SECURITY_CONTEXT");
        assertNotNull(sessionAttribute); // SecurityContext가 저장되었는지 확인
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

        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "", "image/jpeg",
            new byte[0]);

        //given
        final MemberJoinRequest request = MemberJoinRequest.builder().email("test2@example.com")
            .nickname("TestUser2").password("password123").profileImageUrl(profileImage).build();
        final Member expectedMember = Member.builder().memberId(id).email("test2@example.com")
            .nickname("TestUser2").profileImageUrl(null).memberStatus(MemberStatus.ACTIVITY)
            .role(MemberRole.USER).build();

        when(memberRepository.save(any(Member.class))).thenReturn(expectedMember);

        //when
        MemberResponse actualResponse = memberService.joinMember(request);

        //then
        assertThat(actualResponse.getEmail()).isEqualTo(expectedMember.getEmail());
        assertThat(actualResponse.getNickname()).isEqualTo(expectedMember.getNickname());
        assertThat(actualResponse.getProfileImageUrl()).isEqualTo(
            expectedMember.getProfileImageUrl());
        assertThat(actualResponse.getMemberStatus()).isEqualTo(expectedMember.getMemberStatus());
        assertThat(actualResponse.getRole()).isEqualTo(expectedMember.getRole());

        verify(memberRepository, times(1)).save(any(Member.class));
    }

}