package org.programmers.signalbuddy.domain.feedback.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.entity.enums.AnswerStatus;
import org.programmers.signalbuddy.domain.member.MemberRole;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedbackJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Page<FeedbackResponse> fullTextSearch(Pageable pageable, String keyword, Long answerStatus) {
        StringBuilder query = new StringBuilder();
        query
            .append(
                "SELECT f.feedback_id, f.subject, f.content, f.like_count, f.answer_status, f.created_at, f.updated_at, ")
            .append(
                "m.member_id, m.email, m.nickname, m.profile_image_url, m.role, m.member_status ")
            .append("FROM feedbacks f ")
            .append("JOIN members m ON f.member_id = m.member_id ")
            .append("WHERE MATCH(f.subject, f.content) AGAINST (? IN NATURAL LANGUAGE MODE) ")
            .append("AND ")
            .append(answerStatusCondition(answerStatus))
            .append("LIMIT ? OFFSET ?;");

        StringBuilder countQuery = new StringBuilder();
        countQuery
            .append("SELECT count(*) ")
            .append("FROM feedbacks f ")
            .append("JOIN members m ON f.member_id = m.member_id ")
            .append("WHERE MATCH(f.subject, f.content) AGAINST (? IN NATURAL LANGUAGE MODE) ")
            .append("AND ")
            .append(answerStatusCondition(answerStatus));

        List<FeedbackResponse> feedbacks = jdbcTemplate.query(
            query.toString(), new FeedbackResponseRowMapper(), keyword, pageable.getPageSize(),
            pageable.getOffset());

        long total = Optional.ofNullable(
                jdbcTemplate.queryForObject(countQuery.toString(), Long.class, keyword))
            .orElse(0L);

        return new PageImpl<>(feedbacks, pageable, total);
    }

    private String answerStatusCondition(Long answerStatus) {
        String predicate = " ";

        if (answerStatus == 0) {    // 답변 전
            predicate = "answer_status = 'BEFORE' ";
        }
        if (answerStatus == 1) { // 답변 완료
            predicate = "answer_status = 'COMPLETION' ";
        }

        return predicate;
    }

    private static class FeedbackResponseRowMapper implements RowMapper<FeedbackResponse> {

        @Override
        public FeedbackResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            MemberResponse memberResponse = MemberResponse.builder()
                .memberId(rs.getLong("member_id"))
                .email(rs.getString("email"))
                .nickname(rs.getString("nickname"))
                .profileImageUrl(rs.getString("profile_image_url"))
                .role(MemberRole.valueOf(rs.getString("role")))
                .memberStatus(MemberStatus.valueOf(rs.getString("member_status")))
                .build();

            FeedbackResponse feedbackResponse = FeedbackResponse.builder()
                .feedbackId(rs.getLong("feedback_id"))
                .subject(rs.getString("subject"))
                .content(rs.getString("content"))
                .likeCount(rs.getLong("like_count"))
                .answerStatus(AnswerStatus.valueOf(rs.getString("answer_status")))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .member(memberResponse)
                .build();

            return feedbackResponse;
        }
    }
}
