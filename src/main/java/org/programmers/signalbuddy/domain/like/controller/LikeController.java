package org.programmers.signalbuddy.domain.like.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.like.service.LikeService;
import org.programmers.signalbuddy.global.annotation.CurrentUser;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "좋아요 API")
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{feedbackId}")
    public ResponseEntity<Void> addLike(@PathVariable("feedbackId") Long feedbackId,
        @CurrentUser CustomUser2Member user) {
        likeService.addLike(feedbackId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
