package org.programmers.signalbuddy.domain.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.service.FeedbackService;
import org.programmers.signalbuddy.global.annotation.CurrentUser;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        @CurrentUser CustomUser2Member user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(feedbackService.writeFeedback(feedbackWriteRequest, user));
    }

    @Operation(summary = "관리자 피드백 목록 조회")
    @GetMapping("/admin")
    public ResponseEntity<PageResponse<FeedbackResponse>> searchFeedbackList(
        @PageableDefault(page = 0, size = 10, sort = "createAt", direction = Direction.DESC) Pageable pageable,
        @RequestParam(required = false, name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(required = false, name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        @RequestParam(required = false, name = "answerStatus", defaultValue = "-1") Long answerStatus) {
        return ResponseEntity.ok()
            .body(feedbackService.searchFeedbackList(pageable, startDate, endDate, answerStatus));
    }

    @Operation(summary = "관리자 피드백 검색")
    @GetMapping(value = "/admin", params = "keyword")
    public ResponseEntity<PageResponse<FeedbackResponse>> searchFeedbackList(
        @PageableDefault(page = 0, size = 10, sort = "createAt", direction = Direction.DESC) Pageable pageable,
        @RequestParam(name = "keyword") String keyword,
        @RequestParam(required = false, name = "answerStatus", defaultValue = "-1") Long answerStatus) {
        return ResponseEntity.ok()
            .body(feedbackService.searchByKeyword(pageable, keyword, answerStatus));
    }

    @Operation(summary = "피드백 목록 조회")
    @GetMapping
    public ResponseEntity<PageResponse<FeedbackResponse>> searchFeedbackList(
        @PageableDefault(page = 0, size = 10) Pageable pageable,
        @RequestParam(required = false, name = "answerStatus", defaultValue = "-1") Long answerStatus) {
        return ResponseEntity.ok().body(feedbackService.searchFeedbackList(pageable, answerStatus));
    }

    @Operation(summary = "피드백 상세 조회")
    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackResponse> searchFeedbackDetail(
        @PathVariable("feedbackId") Long feedbackId) {
        return ResponseEntity.ok().body(feedbackService.searchFeedbackDetail(feedbackId));
    }

    @Operation(summary = "피드백 수정")
    @PatchMapping("/{feedbackId}")
    public ResponseEntity<Void> updateFeedback(@PathVariable("feedbackId") Long feedbackId,
        @RequestBody @Valid FeedbackWriteRequest feedbackWriteRequest,
        @CurrentUser CustomUser2Member user) {
        feedbackService.updateFeedback(feedbackId, feedbackWriteRequest, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "피드백 삭제")
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable("feedbackId") Long feedbackId,
        @CurrentUser CustomUser2Member user) {
        feedbackService.deleteFeedback(feedbackId, user);
        return ResponseEntity.ok().build();
    }
}
