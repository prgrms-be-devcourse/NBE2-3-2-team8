package org.programmers.signalbuddy.domain.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter // TODO: REST 통신만 할 때는 제거
@Getter
@AllArgsConstructor
@NoArgsConstructor  // TODO: REST 통신만 할 때는 private으로 설정
public class FeedbackWriteRequest {

    @NotBlank(message = "피드백 제목은 비어있을 수 없습니다.")
    private String subject;

    @NotBlank(message = "피드백 내용은 비어있을 수 없습니다.")
    private String content;
}
