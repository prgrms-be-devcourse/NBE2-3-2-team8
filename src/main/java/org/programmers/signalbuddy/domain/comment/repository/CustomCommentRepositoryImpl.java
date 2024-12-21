package org.programmers.signalbuddy.domain.comment.repository;

import static org.programmers.signalbuddy.domain.comment.entity.QComment.comment;
import static org.programmers.signalbuddy.domain.member.entity.QMember.member;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.dto.CommentResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private static final QBean<MemberResponse> memberResponseDto = Projections.fields(
        MemberResponse.class, member.memberId, member.email, member.nickname,
        member.profileImageUrl, member.role, member.memberStatus);

    private static final QBean<CommentResponse> commentResponseDto = Projections.fields(
        CommentResponse.class, comment.commentId, comment.content, comment.createdAt,
        comment.updatedAt, memberResponseDto.as("member"));

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentResponse> findAllByFeedbackIdAndActiveMembers(Long feedbackId,
        Pageable pageable) {
        List<CommentResponse> results = jpaQueryFactory
            .select(commentResponseDto).from(comment)
            .join(member).on(comment.member.eq(member)).fetchJoin()
            .where(member.memberStatus.eq(MemberStatus.ACTIVITY))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(new OrderSpecifier<>(Order.ASC, comment.createdAt)).fetch();

        long count = Optional.ofNullable(
            jpaQueryFactory
                .select(comment.count()).from(comment)
                .join(member).on(comment.member.eq(member)).fetchJoin()
                .where(member.memberStatus.eq(MemberStatus.ACTIVITY)).fetchOne()
            ).orElse(0L);

        return new PageImpl<>(results, pageable, count);
    }
}
