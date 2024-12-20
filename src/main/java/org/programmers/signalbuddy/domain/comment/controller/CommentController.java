package org.programmers.signalbuddy.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.dto.CommentRequest;
import org.programmers.signalbuddy.domain.comment.service.CommentService;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
        User user) {    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
        commentService.writeComment(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> modifyComment(@PathVariable("commentId") Long commentId,
        @RequestBody @Valid CommentRequest request,
        User user) {    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
        commentService.updateComment(commentId, request, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId,
        User user) {    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
        commentService.deleteComment(commentId, user);
        return ResponseEntity.ok().build();
    }
}
