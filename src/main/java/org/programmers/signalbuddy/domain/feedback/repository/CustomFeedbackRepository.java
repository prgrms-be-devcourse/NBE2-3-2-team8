package org.programmers.signalbuddy.domain.feedback.repository;

import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomFeedbackRepository {

    Page<FeedbackResponse> findAllByActiveMembers(Pageable pageable);
}
