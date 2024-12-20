package org.programmers.signalbuddy.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequest {

    @NotNull(message = "피드백 ID 값은 필수입니다.")
    private Long feedbackId;

    @NotBlank(message = "댓글 내용은 비어있을 수 없습니다.")
    private String content;
}
