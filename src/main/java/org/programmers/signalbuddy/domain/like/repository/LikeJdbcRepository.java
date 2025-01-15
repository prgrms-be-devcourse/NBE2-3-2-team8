package org.programmers.signalbuddy.domain.like.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.like.dto.LikeUpdateRequest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAllInBatch(List<LikeUpdateRequest> likeUpdateRequests) {
        String sql = "INSERT INTO likes (member_id, feedback_id, created_at, updated_at) "
            + "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LocalDateTime now = LocalDateTime.now();
                LikeUpdateRequest request = likeUpdateRequests.get(i);
                ps.setLong(1, request.getMemberId());
                ps.setLong(2, request.getFeedbackId());
                ps.setObject(3, now);
                ps.setObject(4, now);
            }

            @Override
            public int getBatchSize() {
                return likeUpdateRequests.size();
            }
        });
    }

    public void deleteAllByLikeRequestsInBatch(List<LikeUpdateRequest> likeUpdateRequests) {
        String sql = "DELETE FROM likes WHERE member_id = ? AND feedback_id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LikeUpdateRequest request = likeUpdateRequests.get(i);
                ps.setLong(1, request.getMemberId());
                ps.setLong(2, request.getFeedbackId());
            }

            @Override
            public int getBatchSize() {
                return likeUpdateRequests.size();
            }
        });
    }
}
