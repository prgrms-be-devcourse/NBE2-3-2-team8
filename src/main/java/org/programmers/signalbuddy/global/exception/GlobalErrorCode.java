package org.programmers.signalbuddy.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GlobalErrorCode implements ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 80000, "잘못된 요청입니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 90000, "알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");

    private HttpStatus httpStatus;
    private int code;
    private String message;
}
