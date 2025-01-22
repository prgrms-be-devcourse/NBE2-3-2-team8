package org.programmers.signalbuddy.domain.admin.batch;

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
public class Scheduler {

    private final JobLauncher jobLauncher;
    private final Job deleteMemberJob;

    @Scheduled(cron = "0 59 23 * * ?")
    @SchedulerLock(
        name = "DeleteMemberJobScheduler",
        lockAtMostFor = "23h",
        lockAtLeastFor = "23h")
    public void runJob() throws Exception {

        JobParameters params = new JobParametersBuilder()
            .toJobParameters();
        jobLauncher.run(deleteMemberJob, params);
    }
}
