package org.programmers.signalbuddy.global.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchLogJob {

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public Job oldBatchLogDeleteJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {

        return new JobBuilder("oldBatchLogDeleteJob", jobRepository)
            .incrementer(new RunIdIncrementer())    // 동일한 파라미터를 다시 실행
            .start(deleteOldLog(jobRepository, transactionManager))
            .build();
    }

    @Bean
    @JobScope
    public Step deleteOldLog(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {

        return new StepBuilder("deleteOldLog", jobRepository)
            .tasklet(new OldLogDeleteTasklet(jdbcTemplate), transactionManager)
            .allowStartIfComplete(true) // COMPLETED 되어도 재실행에 포함시키기
            .build();
    }
}
