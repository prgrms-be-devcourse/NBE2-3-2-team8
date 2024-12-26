package org.programmers.signalbuddy.domain.feedback.repository;

import java.time.LocalDate;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFeedbackRepository {

    Page<FeedbackResponse> findAllByActiveMembers(Pageable pageable, Long answerStatus);

    Page<FeedbackResponse> findPagedByMember(Long memberId, Pageable pageable);

    Page<FeedbackResponse> findAll(Pageable pageable, LocalDate startDate, LocalDate endDate, Long answerStatus);
}
