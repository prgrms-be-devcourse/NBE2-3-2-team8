package org.programmers.signalbuddy.domain.member.batch;


import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.QMember;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@Slf4j
public class JobConfig {


    private final DataSource dataSource;

    private static final int chunkSize = 5;
    private final MemberRepository memberRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final JPAQueryFactory jpaQueryFactory;

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
            transactionManager).reader(deleteMemberReader()).writer(deleteMemberWriter()).build();
    }


    @Bean
    @StepScope
    public ItemReader<Member> deleteMemberReader() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);

        return new ItemReader<>() {
            private final QMember qMember = QMember.member;
            private List<Member> members;
            private int index = 0;

            @Override
            public Member read() {
                if (members == null) {
                    members = jpaQueryFactory.selectFrom(qMember).where(
                        qMember.memberStatus.eq(MemberStatus.WITHDRAWAL)
                            .and(qMember.updatedAt.loe(sixMonthsAgo))).fetch();
                    index = 0;
                }

                if (index < members.size()) {
                    return members.get(index++);
                } else {
                    return null;
                }
            }
        };
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
