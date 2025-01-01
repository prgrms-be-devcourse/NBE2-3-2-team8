package org.programmers.signalbuddy.domain.member.repository;

import org.programmers.signalbuddy.domain.admin.dto.WithdrawalMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMemberRepository {
    Page<WithdrawalMemberResponse> findAllWithdrawMembers(Pageable pageable);
}
