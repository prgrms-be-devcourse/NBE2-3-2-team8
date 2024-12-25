package org.programmers.signalbuddy.domain.member.batch;

import static org.programmers.signalbuddy.domain.member.entity.QMember.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.QMember;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class MemberBatchRepository extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberBatchRepository(JPAQueryFactory queryFactory) {
        super(Member.class);
        this.jpaQueryFactory = queryFactory;
    }

    QMember qMember = member;

    public List<Member> findPageByMemberId(int pageSize, long offset) {
        BooleanBuilder dynamicLtId = new BooleanBuilder();

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(qMember.memberStatus.eq(MemberStatus.WITHDRAWAL));

        whereCondition.and(member.memberStatus.eq(MemberStatus.WITHDRAWAL))
            .and(member.updatedAt.loe(
                LocalDateTime.now().minusMonths(6)));

        return jpaQueryFactory
            .selectFrom(qMember)
            .where(dynamicLtId.and(whereCondition))
            .orderBy(qMember.memberId.asc())
            .limit(pageSize)
            .offset(offset)
            .fetch();
    }

}
