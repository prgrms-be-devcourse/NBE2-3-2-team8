package org.programmers.signalbuddy.domain.member.batch;


import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.entity.Member;

import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobConfig {

    private static final int chunkSize = 10;
    private final MemberRepository memberRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job deleteMemberJob(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new JobBuilder("deleteMemberJob", jobRepository).incrementer(new RunIdIncrementer())
            .start(deleteMemberStep(jobRepository, transactionManager)).build();
    }

    @Bean
    @JobScope
    public Step deleteMemberStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteMemberStep", jobRepository).<Member, Member>chunk(chunkSize,
            transactionManager).reader(customReader()).writer(deleteMemberWriter()).build();
    }


    @Bean
    @StepScope
    public CustomReader customReader() {
        return new CustomReader(entityManagerFactory, chunkSize);
    }

    @Bean
    @StepScope
    public ItemWriter<Member> deleteMemberWriter() {
        return items -> {
            if (items != null && !items.isEmpty()) {
                items.forEach(member -> memberRepository.deleteById(member.getMemberId()));
            }
        };
    }
}
