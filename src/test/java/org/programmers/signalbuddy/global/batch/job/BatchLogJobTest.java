package org.programmers.signalbuddy.global.batch.job;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.global.support.BatchTest;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

class BatchLogJobTest extends BatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job oldBatchLogDeleteJob;

    @DisplayName("14일 이상된 배치 작업의 로그를 삭제하는 잡 실행")
    @Test
    void oldBatchLogDeleteJob() throws Exception {
        // when
        jobLauncherTestUtils.setJob(oldBatchLogDeleteJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}