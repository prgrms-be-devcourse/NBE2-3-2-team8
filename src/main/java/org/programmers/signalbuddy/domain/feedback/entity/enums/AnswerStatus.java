package org.programmers.signalbuddy.domain.feedback.entity.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AnswerStatus {

    BEFORE("답변 전"), COMPLETION("답변 완료");

    private final String status;
}
