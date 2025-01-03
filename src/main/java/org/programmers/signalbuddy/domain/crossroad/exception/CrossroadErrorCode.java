package org.programmers.signalbuddy.domain.crossroad.exception;

import org.programmers.signalbuddy.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CrossroadErrorCode implements ErrorCode {

    CROSSROAD_API_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 30000, "교차로 API 요청이 실패했습니다."),
    ALREADY_EXIST_CROSSROAD(HttpStatus.CONFLICT, 30001, "이미 존재하는 교차로입니다.");

    private HttpStatus httpStatus;
    private int code;
    private String message;
}
