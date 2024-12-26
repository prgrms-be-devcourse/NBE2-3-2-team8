package org.programmers.signalbuddy.domain.member.batch;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.QMember;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.springframework.batch.item.ItemReader;

import java.util.List;

@Slf4j
public class CustomReader implements ItemReader<Member> {

    private final JPAQueryFactory queryFactory;
    private final int pageSize;
    private Long lastSeenId;
    private List<Member> currentPage;
    private int currentIndex;

    public CustomReader(EntityManagerFactory entityManagerFactory, int pageSize) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.pageSize = pageSize;
        this.lastSeenId = null;
        this.currentIndex = 0;
    }

    @Override
    public Member read() {
        if (currentPage == null || currentIndex >= currentPage.size()) {
            fetchNextPage();
        }

        if (currentPage == null || currentPage.isEmpty()) {
            return null;
        }

        Member member = currentPage.get(currentIndex);
        currentIndex++;
        return member;
    }

    private void fetchNextPage() {
        QMember qMember = QMember.member;

        currentPage = queryFactory
            .selectFrom(qMember)
            .where(
                lastSeenId != null ? qMember.memberId.gt(lastSeenId) : null,
                qMember.memberStatus.eq(MemberStatus.WITHDRAWAL).and(qMember.updatedAt.loe(
                    LocalDateTime.now().minusMonths(6)))
            ).orderBy(qMember.memberId.asc())
            .offset(0)
            .limit(pageSize)
            .fetch();
        // log.info("마지막으로 읽은 id: " + lastSeenId);

        if (!currentPage.isEmpty()) {
            lastSeenId = currentPage.get(currentPage.size() - 1).getMemberId();
        }
        currentIndex = 0;
    }

}
