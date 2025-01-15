package org.programmers.signalbuddy.domain.like.batch;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.like.dto.LikeUpdateRequest;
import org.programmers.signalbuddy.domain.like.repository.LikeJdbcRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class LikeJobConfig {

    private final StringRedisTemplate redisTemplate;
    private final FeedbackRepository feedbackRepository;
    private final LikeJdbcRepository likeJdbcRepository;

    @Bean
    public Job likeRequestJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {

        return new JobBuilder("likeRequestJob", jobRepository)
            .incrementer(new RunIdIncrementer())    // 동일한 파라미터를 다시 실행
            .start(updateLikeBatch(jobRepository, transactionManager))
            .build();
    }

    @Bean
    @JobScope
    public Step updateLikeBatch(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {

        return new StepBuilder("updateLikeBatch", jobRepository)
            .<LikeUpdateRequest, LikeUpdateRequest>chunk(100, transactionManager)
            .reader(requestLikeReader())
            .writer(requestLikeWriter())
            .allowStartIfComplete(true) // COMPLETED 되어도 재실행에 포함시키기
            .build();
    }

    @Bean
    @StepScope
    public RequestLikeReader requestLikeReader() {
        return new RequestLikeReader(redisTemplate);
    }

    @Bean
    @StepScope
    public RequestLikeWriter requestLikeWriter() {
        return new RequestLikeWriter(feedbackRepository, likeJdbcRepository, redisTemplate);
    }
}
