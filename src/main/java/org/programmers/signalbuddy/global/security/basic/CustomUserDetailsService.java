package org.programmers.signalbuddy.global.security.basic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member findMember = memberRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.info("인증 실패: {}", email);
                return new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER);
            });

        final MemberStatus memberStatus = findMember.getMemberStatus();
        if (memberStatus == MemberStatus.WITHDRAWAL) {
            throw new BusinessException(MemberErrorCode.WITHDRAWN_MEMBER);
        }

        return new CustomUserDetails(findMember.getMemberId(), findMember.getEmail(),
            findMember.getPassword(), findMember.getProfileImageUrl(), findMember.getNickname(),
            findMember.getRole(), findMember.getMemberStatus());
    }
}
