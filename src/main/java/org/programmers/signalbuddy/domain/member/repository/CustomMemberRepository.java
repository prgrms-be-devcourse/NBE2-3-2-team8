package org.programmers.signalbuddy.domain.member.repository;

import org.programmers.signalbuddy.domain.admin.dto.WithdrawalMemberResponse;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMemberRepository {
    Page<Member> findAllMembers(Pageable pageable);
    Page<WithdrawalMemberResponse> findAllWithdrawMembers(Pageable pageable);
}
