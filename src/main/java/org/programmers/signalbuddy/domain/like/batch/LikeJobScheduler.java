package org.programmers.signalbuddy.domain.like.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class LikeJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job likeRequestJob;

    @Scheduled(cron = "${schedule.like-job.cron}")
    public void runJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .toJobParameters();
        jobLauncher.run(likeRequestJob, params);
    }
}
