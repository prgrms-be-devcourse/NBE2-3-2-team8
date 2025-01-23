package org.programmers.signalbuddy.domain.member.service;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    public MemberResponse updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        final Member member = memberRepository.findById(id)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
        final String encodedPassword = bCryptPasswordEncoder.encode(
            memberUpdateRequest.getPassword());
        member.updateMember(memberUpdateRequest, encodedPassword);
        log.info("Member updated: {}", member);
        return MemberMapper.INSTANCE.toDto(member);
    }

    @Transactional
    public MemberResponse deleteMember(Long id) {
        final Member member = memberRepository.findById(id)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
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

        String profilePath = "none";

        if (!memberJoinRequest.getProfileImageUrl().isEmpty()) {
            profilePath = saveProfileImage(memberJoinRequest.getProfileImageUrl());
        }

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
        final Member member = memberRepository.findById(user.getMemberId())
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));

        return bCryptPasswordEncoder.matches(password, member.getPassword());
    }
}
