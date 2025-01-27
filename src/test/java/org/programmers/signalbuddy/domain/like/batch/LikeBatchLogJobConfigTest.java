package org.programmers.signalbuddy.domain.like.batch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.programmers.signalbuddy.global.support.BatchTest;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;

class LikeBatchLogJobConfigTest extends BatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private Job likeBatchLogDeleteJob;

    @DisplayName("좋아요 배치 작업의 로그 삭제 잡 실행")
    @Test
    void likeBatchLogDeleteJob() throws Exception {
        // when
        jobLauncherTestUtils.setJob(likeBatchLogDeleteJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}