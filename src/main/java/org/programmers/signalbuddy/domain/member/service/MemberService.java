package org.programmers.signalbuddy.domain.member.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
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
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    @Value("${file-path}")
    private String filePath;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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
            .profileImageUrl(profilePath)
            .memberStatus(MemberStatus.ACTIVITY).role(MemberRole.USER).build();

        memberRepository.save(joinMember);
        return MemberMapper.INSTANCE.toDto(joinMember);
    }

    public Resource getProfileImage(String filename) {
        try {
            Path directoryPath = Paths.get("src", "main", "resources", "static", "images");
            final Path path = Paths.get(directoryPath.toString()).resolve(filename);
            if (Files.notExists(path)) {
                return new ClassPathResource("static/images/member/profile-icon.png");
                // 프로필 이미지가 없을 경우 기본 이미지
            }
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new BusinessException(MemberErrorCode.PROFILE_IMAGE_LOAD_ERROR);
        }
    }

    public String saveProfileImage(MultipartFile profileImage) {

        // static/images 경로 설정
        Path directoryPath = Paths.get("src", "main", "resources", "static", "images");

        String originalFilename = profileImage.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String newFilename = UUID.randomUUID().toString() + extension;
        Path savePath = directoryPath.resolve(newFilename);

        try {
            Files.copy(profileImage.getInputStream(), savePath,
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error while saving profile image: ", e);
            throw new BusinessException(MemberErrorCode.PROFILE_IMAGE_UPLOAD_FAILURE);
        }

        return newFilename.toString();
    }

    public boolean verifyPassword(String password, CustomUser2Member user) {
        final Member member = memberRepository.findById(user.getMemberId())
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));

        return bCryptPasswordEncoder.matches(password, member.getPassword());
    }
}
