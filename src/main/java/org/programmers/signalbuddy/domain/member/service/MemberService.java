package org.programmers.signalbuddy.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMember(Long id) {
        return memberRepository.findById(id).map(MemberMapper.INSTANCE::toDto)
            .orElseThrow(() -> new RuntimeException("Not Yet"));
    }
}
