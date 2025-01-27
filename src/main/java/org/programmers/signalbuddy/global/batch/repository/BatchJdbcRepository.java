package org.programmers.signalbuddy.global.batch.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.global.batch.dto.BatchExecutionId;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * STEP_EXECUTION_ID로 STEP 관련 BATCH META TABLE의 데이터를 벌크 연산으로 삭제
     *
     * @param batchTableName 삭제할 배치 테이블명
     * @param executionIds  STEP_EXECUTION_ID, JOB_EXECUTION_ID 목록
     */
    public void deleteAllByStepExecutionIdInBatch(String batchTableName, List<BatchExecutionId> executionIds) {
        String sql = "DELETE FROM " + batchTableName + " WHERE STEP_EXECUTION_ID = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long stepExecutionId = executionIds.get(i).getStepExecutionId();
                ps.setLong(1, stepExecutionId);
            }

            @Override
            public int getBatchSize() {
                return executionIds.size();
            }
        });
    }

    /**
     * JOB_EXECUTION_ID로 JOB 관련 BATCH META TABLE의 데이터를 벌크 연산으로 삭제
     *
     * @param batchTableName 삭제할 배치 테이블명
     * @param executionIds  STEP_EXECUTION_ID, JOB_EXECUTION_ID 목록
     */
    public void deleteAllByJobExecutionIdInBatch(String batchTableName, List<BatchExecutionId> executionIds) {
        String sql = "DELETE FROM " + batchTableName + " WHERE JOB_EXECUTION_ID = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long jobExecutionId = executionIds.get(i).getJobExecutionId();
                ps.setLong(1, jobExecutionId);
            }

            @Override
            public int getBatchSize() {
                return executionIds.size();
            }
        });
    }
}
