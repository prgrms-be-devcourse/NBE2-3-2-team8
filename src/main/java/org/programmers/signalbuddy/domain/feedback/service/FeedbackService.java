package org.programmers.signalbuddy.domain.feedback.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
}
