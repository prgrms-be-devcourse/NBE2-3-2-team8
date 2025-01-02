package org.programmers.signalbuddy.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.dto.CommentResponse;
import org.programmers.signalbuddy.domain.comment.service.CommentService;
import org.programmers.signalbuddy.global.annotation.CurrentUser;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.dto.PageResponse;
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

@Tag(name = "Comment API")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping("/write")
    public ResponseEntity<Void> writeComment(@RequestBody @Valid CommentRequest request,
        @CurrentUser CustomUser2Member user) {
        commentService.writeComment(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "댓글 목록 조회")
    @GetMapping("/{feedbackId}")
    public ResponseEntity<PageResponse<CommentResponse>> searchCommentList(
        @PathVariable("feedbackId") Long feedbackId,
        @PageableDefault(page = 0, size = 7)Pageable pageable) {
        return ResponseEntity.ok(commentService.searchCommentList(feedbackId , pageable));
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> modifyComment(@PathVariable("commentId") Long commentId,
        @RequestBody @Valid CommentRequest request,
        @CurrentUser CustomUser2Member user) {
        commentService.updateComment(commentId, request, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId,
        @CurrentUser CustomUser2Member user) {
        commentService.deleteComment(commentId, user);
        return ResponseEntity.ok().build();
    }
}
