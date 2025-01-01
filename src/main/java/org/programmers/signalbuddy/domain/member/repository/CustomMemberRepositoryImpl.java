package org.programmers.signalbuddy.domain.member.repository;

import static org.programmers.signalbuddy.domain.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.admin.dto.WithdrawalMemberResponse;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private static final QBean<WithdrawalMemberResponse> withdrawalMemberResponseDto = Projections.fields(
        WithdrawalMemberResponse.class, member.memberId, member.email, member.nickname, member.profileImageUrl, member.role, member.memberStatus,
        member.createdAt, member.updatedAt);

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<WithdrawalMemberResponse> findAllWithdrawMembers(Pageable pageable) {
        List<WithdrawalMemberResponse> members = jpaQueryFactory
            .select(withdrawalMemberResponseDto)
            .from(member)
            .where(member.memberStatus.eq(MemberStatus.WITHDRAWAL))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = jpaQueryFactory
            .select(withdrawalMemberResponseDto)
            .from(member)
            .where(member.memberStatus.eq(MemberStatus.WITHDRAWAL))
            .fetchCount();

        return new PageImpl<>(members, pageable, total);
    }
}
