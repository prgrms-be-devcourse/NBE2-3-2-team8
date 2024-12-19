package org.programmers.signalbuddy.domain.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackWriteRequest {

    @NotBlank(message = "피드백 제목은 비어있을 수 없습니다.")
    private String subject;

    @NotBlank(message = "피드백 내용은 비어있을 수 없습니다.")
    private String content;
}
