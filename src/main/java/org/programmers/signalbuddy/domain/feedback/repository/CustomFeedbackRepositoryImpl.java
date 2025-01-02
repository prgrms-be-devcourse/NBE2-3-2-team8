package org.programmers.signalbuddy.domain.feedback.repository;

import static org.programmers.signalbuddy.domain.feedback.entity.QFeedback.feedback;
import static org.programmers.signalbuddy.domain.member.entity.QMember.member;
import static org.programmers.signalbuddy.global.util.QueryDSLUtil.betweenDates;
import static org.programmers.signalbuddy.global.util.QueryDSLUtil.getOrderSpecifiers;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.entity.enums.AnswerStatus;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomFeedbackRepositoryImpl implements CustomFeedbackRepository {

    private static final QBean<MemberResponse> memberResponseDto = Projections.fields(
        MemberResponse.class, member.memberId, member.email, member.nickname,
        member.profileImageUrl, member.role, member.memberStatus);

    private static final QBean<FeedbackResponse> feedbackResponseDto = Projections.fields(
        FeedbackResponse.class, feedback.feedbackId, feedback.subject, feedback.content,
        feedback.likeCount, feedback.answerStatus, feedback.createdAt, feedback.updatedAt,
        memberResponseDto.as("member"));

    private static final QBean<FeedbackResponse> feedbackNoMemberDto = Projections.fields(
        FeedbackResponse.class, feedback.feedbackId, feedback.subject, feedback.content,
        feedback.likeCount, feedback.createdAt, feedback.updatedAt);

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FeedbackResponse> findAllByActiveMembers(Pageable pageable, Long answerStatus) {
        BooleanExpression answerStatusCondition = answerStatusCondition(answerStatus);

        List<FeedbackResponse> results = jpaQueryFactory
            .select(feedbackResponseDto).from(feedback)
            .join(member).on(feedback.member.eq(member)).fetchJoin()
            .where(member.memberStatus.eq(MemberStatus.ACTIVITY).and(answerStatusCondition))
            .offset(pageable.getOffset()).limit(pageable.getPageSize())
            .orderBy(new OrderSpecifier<>(Order.DESC, feedback.createdAt)).fetch();

        long count = Optional.ofNullable(
            jpaQueryFactory.select(feedback.count()).from(feedback)
                .join(member).on(feedback.member.eq(member)).fetchJoin()
                .where(member.memberStatus.eq(MemberStatus.ACTIVITY).and(answerStatusCondition))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public Page<FeedbackResponse> findPagedByMember(Long memberId, Pageable pageable) {
        final List<FeedbackResponse> responses = jpaQueryFactory.select(feedbackResponseDto)
            .from(feedback).join(member)
            .on(feedback.member.eq(member).and(member.memberId.eq(memberId)))
            .offset(pageable.getOffset()).limit(pageable.getPageSize())
            .orderBy(new OrderSpecifier<>(Order.DESC, feedback.createdAt)).fetch();
        final Long count = jpaQueryFactory.select(feedback.count()).from(feedback).join(member)
            .on(member.memberId.eq(memberId)).fetchOne();
        return new PageImpl<>(responses, pageable, count != null ? count : 0);
    }

    @Override
    public Page<FeedbackResponse> findAll(Pageable pageable,
        LocalDate startDate, LocalDate endDate, Long answerStatus) {

        BooleanExpression betweenDates = betweenDates(feedback.createdAt, startDate, endDate);
        BooleanExpression answerStatusCondition = answerStatusCondition(answerStatus);

        List<FeedbackResponse> results = jpaQueryFactory
            .select(feedbackResponseDto).from(feedback)
            .join(member).on(feedback.member.eq(member)).fetchJoin()
            .where(betweenDates.and(answerStatusCondition(answerStatus)))
            .offset(pageable.getOffset()).limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable, feedback.getType(), "feedback")).fetch();

        long count = Optional.ofNullable(
            jpaQueryFactory
                .select(feedback.count()).from(feedback)
                .join(member).on(feedback.member.eq(member)).fetchJoin()
                .where(betweenDates.and(answerStatusCondition)).fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, count);
    }

    private BooleanExpression answerStatusCondition(Long answerStatus) {
        BooleanExpression predicate = null;

        if (answerStatus == 0) {    // 답변 전
            predicate = feedback.answerStatus.eq(AnswerStatus.BEFORE);
        }
        if (answerStatus == 1) { // 답변 완료
            predicate = feedback.answerStatus.eq(AnswerStatus.COMPLETION);
        }

        return predicate != null ? predicate : Expressions.TRUE;
    }
}
