package org.programmers.signalbuddy.domain.comment.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.programmers.signalbuddy.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommentErrorCode implements ErrorCode {

    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, 20000, "해당 댓글은 찾을 수 없습니다."),
    COMMENT_MODIFIER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, 20001, "해당 댓글의 작성자와 일치하지 않습니다.");

    private HttpStatus httpStatus;
    private int code;
    private String message;
}
