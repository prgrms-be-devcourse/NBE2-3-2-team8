package org.programmers.signalbuddy.domain.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.like.dto.LikeExistResponse;
import org.programmers.signalbuddy.domain.like.service.LikeService;
import org.programmers.signalbuddy.global.annotation.CurrentUser;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Like API")
@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 추가")
    @PostMapping("/{feedbackId}/like")
    public ResponseEntity<Void> addLike(@PathVariable("feedbackId") Long feedbackId,
        @CurrentUser CustomUser2Member user) {
        likeService.addLike(feedbackId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "좋아요 유무 확인")
    @GetMapping("/{feedbackId}/exist")
    public ResponseEntity<LikeExistResponse> existsLike(@PathVariable("feedbackId") Long feedbackId,
        @CurrentUser CustomUser2Member user) {
        return ResponseEntity.ok(likeService.existsLike(feedbackId, user));
    }

    @Operation(summary = "좋아요 취소")
    @DeleteMapping("/{feedbackId}/like")
    public ResponseEntity<Void> deleteLike(@PathVariable("feedbackId") Long feedbackId,
        @CurrentUser CustomUser2Member user) {
        likeService.deleteLike(feedbackId, user);
        return ResponseEntity.ok().build();
    }
}
