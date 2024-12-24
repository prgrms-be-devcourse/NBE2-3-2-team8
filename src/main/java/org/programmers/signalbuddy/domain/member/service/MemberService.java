package org.programmers.signalbuddy.domain.member.service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.dto.MemberJoinRequest;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.mapper.MemberMapper;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final String FILE_PATH = "images/"; // TODO : 프로퍼티로 이동

    private final MemberRepository memberRepository;
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
        member.updateMember(memberUpdateRequest);
        log.info("Member updated: {}", member);
        return MemberMapper.INSTANCE.toDto(member);
    }

    @Transactional
    public MemberResponse deleteMember(Long id) {
        // TODO : id 검증 로직 추가
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

        Member joinMember = Member.builder()
            .email(memberJoinRequest.getEmail())
            .nickname(memberJoinRequest.getNickname())
            .password(bCryptPasswordEncoder.encode(memberJoinRequest.getPassword()))
            .profileImageUrl(memberJoinRequest.getProfileImageUrl())
            .memberStatus(MemberStatus.ACTIVITY).role(MemberRole.USER).build();

        memberRepository.save(joinMember);
        return MemberMapper.INSTANCE.toDto(joinMember);
    }

    public Resource getProfileImage(String filename) {
        try {
            final Path path = Paths.get(FILE_PATH).resolve(filename);
            if (Files.notExists(path)) {
                return new ClassPathResource("static/images/member/profile-icon.png");
                // 프로필 이미지가 없을 경우 기본 이미지
            }
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new BusinessException(MemberErrorCode.PROFILE_IMAGE_LOAD_ERROR);
        }
    }
}
