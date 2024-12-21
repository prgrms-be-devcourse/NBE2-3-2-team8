package org.programmers.signalbuddy.domain.feedback.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.entity.enums.AnswerStatus;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class FeedbackResponse {

    private Long feedbackId;
    private String subject;
    private String content;
    private Long likeCount;
    private AnswerStatus answerStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MemberResponse member;
}
