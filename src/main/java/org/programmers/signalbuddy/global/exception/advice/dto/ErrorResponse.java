package org.programmers.signalbuddy.global.exception.advice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.programmers.signalbuddy.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
