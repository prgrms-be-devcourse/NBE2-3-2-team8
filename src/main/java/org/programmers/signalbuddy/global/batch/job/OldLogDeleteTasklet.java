package org.programmers.signalbuddy.global.batch.job;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OldLogDeleteTasklet implements Tasklet {

    private final JdbcTemplate jdbcTemplate;

    private static final long EXPIRED_DATE = 14; // 로그의 유효 기간 (일)

    // Batch Meta Table 로그 삭제 쿼리들 (순서대로 진행해야 함)
    private static final List<String> DELETE_QUERIES = List.of(
        "DELETE FROM BATCH_STEP_EXECUTION_CONTEXT WHERE STEP_EXECUTION_ID IN ( SELECT STEP_EXECUTION_ID FROM BATCH_STEP_EXECUTION WHERE START_TIME < ?)",
        "DELETE FROM BATCH_STEP_EXECUTION WHERE START_TIME < ?",
        "DELETE FROM BATCH_JOB_EXECUTION_CONTEXT WHERE JOB_EXECUTION_ID IN ( SELECT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION WHERE CREATE_TIME < ?)",
        "DELETE FROM BATCH_JOB_EXECUTION_PARAMS WHERE JOB_EXECUTION_ID IN ( SELECT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION WHERE CREATE_TIME < ?)",
        "DELETE FROM BATCH_JOB_EXECUTION WHERE CREATE_TIME < ?",
        "DELETE FROM BATCH_JOB_INSTANCE WHERE JOB_INSTANCE_ID NOT IN (SELECT JOB_INSTANCE_ID FROM BATCH_JOB_EXECUTION)");

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Date oldDate = Date.valueOf(LocalDate.now().minusDays(EXPIRED_DATE));

        for (String query : DELETE_QUERIES) {
            jdbcTemplate.update(query, ps -> ps.setDate(1, oldDate));
        }
        return RepeatStatus.FINISHED;
    }
}
