package org.programmers.signalbuddy.domain.member.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.dto.MemberJoinRequest;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.mapper.MemberMapper;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.programmers.signalbuddy.global.security.basic.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AwsFileService awsFileService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Value("${default.profile.image.path}")
    private String defaultProfileImagePath;

    public MemberResponse getMember(Long id) {
        return memberRepository.findById(id)
            .filter(m -> m.getMemberStatus() == MemberStatus.ACTIVITY)
            .map(MemberMapper.INSTANCE::toDto)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest memberUpdateRequest,
        HttpServletRequest request) {
        final Member member = findMemberById(id);
        final String encodedPassword = encodedPassword(memberUpdateRequest.getPassword());

        final String saveProfileImage = saveProfileImageIfPresent(
            memberUpdateRequest.getImageFile());

        member.updateMember(memberUpdateRequest, encodedPassword, saveProfileImage);
        log.info("Member updated: {}", member);
        updateSecurityContext(member, request);
        return MemberMapper.INSTANCE.toDto(member);
    }

    @Transactional
    public MemberResponse deleteMember(Long id) {
        final Member member = findMemberById(id);
        member.softDelete();
        log.info("Member deleted: {}", member);
        return MemberMapper.INSTANCE.toDto(member);
    }

    @Transactional
    public MemberResponse joinMember(MemberJoinRequest memberJoinRequest) {

        // 이미 존재하는 사용자인지 확인
        if (memberRepository.existsByEmail(memberJoinRequest.getEmail())) {
            throw new BusinessException(MemberErrorCode.ALREADY_EXIST_EMAIL);
        }

        String profilePath = saveProfileImageIfPresent(memberJoinRequest.getProfileImageUrl());

        Member joinMember = Member.builder().email(memberJoinRequest.getEmail())
            .nickname(memberJoinRequest.getNickname())
            .password(bCryptPasswordEncoder.encode(memberJoinRequest.getPassword()))
            .profileImageUrl(profilePath).memberStatus(MemberStatus.ACTIVITY).role(MemberRole.USER)
            .build();

        memberRepository.save(joinMember);
        return MemberMapper.INSTANCE.toDto(joinMember);
    }

    public Resource getProfileImage(String filename) {
        try {
            if (filename.isEmpty()) {
                return new ClassPathResource(defaultProfileImagePath);
            }
            return awsFileService.getProfileImage(filename);
        } catch (BusinessException e) {
            return new ClassPathResource(defaultProfileImagePath);
        }
    }

    public String saveProfileImage(MultipartFile profileImage) {
        return awsFileService.saveProfileImage(profileImage);
    }

    public boolean verifyPassword(String password, CustomUser2Member user) {
        final Member member = findMemberById(user.getMemberId());

        return bCryptPasswordEncoder.matches(password, member.getPassword());
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private String saveProfileImageIfPresent(MultipartFile imageFile) {
        if (imageFile == null) {
            return null;
        }
        return saveProfileImage(imageFile);
    }

    private String encodedPassword(String password) {
        if (password == null) {
            return null;
        }
        return bCryptPasswordEncoder.encode(password);
    }

    private void updateSecurityContext(Member member, HttpServletRequest request) {
        // CustomUserDetails 생성
        final CustomUserDetails userDetails = new CustomUserDetails(
            member.getMemberId(),
            member.getEmail(),
            member.getPassword(),
            member.getProfileImageUrl(),
            member.getNickname(),
            member.getRole(),
            member.getMemberStatus()
        );

        // Authentication 객체 생성
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null, // 비밀번호는 이미 인증되었으므로 null
            userDetails.getAuthorities()
        );

        // SecurityContext 생성 및 Authentication 설정
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        // SecurityContextHolder에 설정
        SecurityContextHolder.setContext(securityContext);

        // HttpSession에 SecurityContext 저장
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        // HttpSession 갱신
        request.getSession().setAttribute("user", userDetails);
    }
}
