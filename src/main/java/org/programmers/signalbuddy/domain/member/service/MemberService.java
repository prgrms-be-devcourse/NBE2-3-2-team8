package org.programmers.signalbuddy.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.mapper.MemberMapper;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMember(Long id) {
        return memberRepository.findById(id).map(MemberMapper.INSTANCE::toDto)
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
}
