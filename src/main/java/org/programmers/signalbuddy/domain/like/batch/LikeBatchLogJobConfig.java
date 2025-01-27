package org.programmers.signalbuddy.domain.like.batch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.global.batch.dto.BatchExecutionId;
import org.programmers.signalbuddy.global.batch.repository.BatchJdbcRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LikeBatchLogJobConfig {

    private final DataSource dataSource;
    private final BatchJdbcRepository batchJdbcRepository;

    @Value("${schedule.like-log-delete-job.expired-minutes}")
    private String expiredMinutes;    // 로그의 유효 시간 (분)

    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job likeBatchLogDeleteJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {

        return new JobBuilder("likeBatchLogDeleteJob", jobRepository)
            .incrementer(new RunIdIncrementer())    // 동일한 파라미터를 다시 실행
            .start(deleteLikeLogBatch(jobRepository, transactionManager))
            .build();
    }

    @Bean
    @JobScope
    public Step deleteLikeLogBatch(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {

        return new StepBuilder("deleteLikeLogBatch", jobRepository)
            .<BatchExecutionId, BatchExecutionId>chunk(CHUNK_SIZE, transactionManager)
            .reader(executionPagingReader())
            .writer(deleteLog())
            .allowStartIfComplete(true) // COMPLETED 되어도 재실행에 포함시키기
            .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<BatchExecutionId> executionPagingReader() {
        return new JdbcPagingItemReaderBuilder<BatchExecutionId>()
            .name("executionPagingReader")
            .dataSource(dataSource)
            .selectClause("SELECT STEP_EXECUTION_ID, JOB_EXECUTION_ID")
            .fromClause("FROM BATCH_STEP_EXECUTION")
            .whereClause("WHERE STEP_NAME IN ('updateLikeBatch', 'deleteLikeLogBatch')"
                + "AND START_TIME < :threshold")
            .parameterValues(Map.of("threshold",
                LocalDateTime.now().minusMinutes(Long.parseLong(expiredMinutes))))
            .sortKeys(Map.of("STEP_EXECUTION_ID", Order.ASCENDING))
            .pageSize(CHUNK_SIZE)
            .rowMapper(new BeanPropertyRowMapper<>(BatchExecutionId.class))
            .build();
    }

    @Bean
    @StepScope
    public ItemWriter<BatchExecutionId> deleteLog() {
        return (chunk) -> {
            @SuppressWarnings("unchecked")
            List<BatchExecutionId> executionIds = (List<BatchExecutionId>) chunk.getItems();

            batchJdbcRepository.deleteAllByStepExecutionIdInBatch(
                "BATCH_STEP_EXECUTION_CONTEXT", executionIds);
            batchJdbcRepository.deleteAllByStepExecutionIdInBatch(
                "BATCH_STEP_EXECUTION", executionIds);
            batchJdbcRepository.deleteAllByJobExecutionIdInBatch(
                "BATCH_JOB_EXECUTION_CONTEXT", executionIds);
            batchJdbcRepository.deleteAllByJobExecutionIdInBatch(
                "BATCH_JOB_EXECUTION", executionIds);
        };
    }
}
