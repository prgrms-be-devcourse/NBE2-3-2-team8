package org.programmers.signalbuddy.domain.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.service.FeedbackService;
import org.programmers.signalbuddy.global.dto.PageResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "피드백 목록")
    @GetMapping
    public ResponseEntity<PageResponse<FeedbackResponse>> searchFeedbackList(
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok().body(feedbackService.searchFeedbackList(pageable));
    }

    @Operation(summary = "피드백 작성")
    @PostMapping("/write")
    public ResponseEntity<FeedbackResponse> writeFeedback(
        @RequestBody @Valid FeedbackWriteRequest feedbackWriteRequest,
        User user) {    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(feedbackService.writeFeedback(feedbackWriteRequest, user));
    }

    @Operation(summary = "피드백 수정")
    @PatchMapping("/{feedbackId}")
    public ResponseEntity<Void> updateFeedback(@PathVariable("feedbackId") Long feedbackId,
        @RequestBody @Valid FeedbackWriteRequest feedbackWriteRequest,
        User user) {    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
        feedbackService.updateFeedback(feedbackId, feedbackWriteRequest, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "피드백 삭제")
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable("feedbackId") Long feedbackId,
        User user) {    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
        feedbackService.deleteFeedback(feedbackId, user);
        return ResponseEntity.ok().build();
    }
}
