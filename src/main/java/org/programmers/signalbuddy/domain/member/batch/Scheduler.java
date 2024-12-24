package org.programmers.signalbuddy.domain.member.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final Job deleteMemberJob;

    @Scheduled(cron = "*/5 * * * * ?")
    public void runJob() throws Exception {

        JobParameters params = new JobParametersBuilder()
            .addLong("timestamp", System.currentTimeMillis())  // 고유한 값 추가
            .toJobParameters();
        jobLauncher.run(deleteMemberJob, params);
    }
}
