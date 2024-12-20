package org.programmers.signalbuddy.domain.feedback.repository;

import static org.programmers.signalbuddy.domain.feedback.entity.QFeedback.feedback;
import static org.programmers.signalbuddy.domain.member.entity.QMember.member;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomFeedbackRepositoryImpl implements CustomFeedbackRepository {

    private static final QBean<FeedbackResponse.MemberResponse> memberResponseDto = Projections.fields(
        FeedbackResponse.MemberResponse.class, member.memberId, member.nickname,
        member.profileImageUrl, member.role, member.memberStatus);

    private static final QBean<FeedbackResponse> feedbackResponseDto = Projections.fields(
        FeedbackResponse.class, feedback.feedbackId, feedback.subject, feedback.content,
        feedback.likeCount, feedback.createdAt, feedback.updatedAt, memberResponseDto.as("member"));

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FeedbackResponse> findAllByActiveMembers(Pageable pageable) {
        List<FeedbackResponse> results = jpaQueryFactory.select(feedbackResponseDto).from(feedback)
            .join(member).on(feedback.member.eq(member)).fetchJoin()
            .where(member.memberStatus.eq(MemberStatus.ACTIVITY))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(new OrderSpecifier<>(Order.DESC, feedback.updatedAt)).fetch();

        Long count = jpaQueryFactory.select(feedback.count()).from(feedback)
            .join(member).on(feedback.member.eq(member)).fetchJoin().fetchOne();

        return new PageImpl<>(results, pageable, (count == null ? 0L : count));
    }
}
