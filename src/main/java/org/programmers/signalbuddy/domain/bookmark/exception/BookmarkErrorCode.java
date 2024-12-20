package org.programmers.signalbuddy.domain.bookmark.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.programmers.signalbuddy.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BookmarkErrorCode implements ErrorCode {

    INVALID_COORDINATES(HttpStatus.BAD_REQUEST, 10001, "위도 또는 경도 값이 유효하지 않습니다.");

    private HttpStatus httpStatus;
    private int code;
    private String message;
}
