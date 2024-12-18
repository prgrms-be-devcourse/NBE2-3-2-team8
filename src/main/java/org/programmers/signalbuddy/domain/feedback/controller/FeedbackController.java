package org.programmers.signalbuddy.domain.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.service.FeedbackService;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Feedback API")
@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "피드백 작성")
    @PostMapping("/write")
    public ResponseEntity<FeedbackResponse> writeFeedback(
        @RequestBody @Valid FeedbackWriteRequest feedbackWriteRequest,
        User user) {    // 인자값에 User 객체는 나중에 변경해야 함!
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(feedbackService.writeFeedback(feedbackWriteRequest, user));
    }
}
