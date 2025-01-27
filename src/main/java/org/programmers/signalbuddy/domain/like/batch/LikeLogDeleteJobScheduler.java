package org.programmers.signalbuddy.domain.like.batch;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class LikeLogDeleteJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job likeBatchLogDeleteJob;

    @Scheduled(cron = "${schedule.like-log-delete-job.cron}")
    @SchedulerLock(
        name = "LikeLogDeleteJobScheduler",
        lockAtMostFor = "${schedule.like-log-delete-job.lockAtMostFor}",
        lockAtLeastFor = "${schedule.like-log-delete-job.lockAtLeastFor}")
    public void runJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
            .toJobParameters();
        jobLauncher.run(likeBatchLogDeleteJob, params);
    }
}
