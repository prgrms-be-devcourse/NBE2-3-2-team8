package org.programmers.signalbuddy.domain.member.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.programmers.signalbuddy.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MemberErrorCode implements ErrorCode {

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, 60000, "해당 사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, 60001, "이미 존재하는 이메일 입니다."),
    PROFILE_IMAGE_LOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 60002, "프로필 이미지 로드 중 오류가 발생했습니다."),
    WITHDRAWN_MEMBER(HttpStatus.FORBIDDEN, 60003, "탈퇴한 회원입니다."),
    PROFILE_IMAGE_UPLOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, 60004, "프로필 이미지 저장 중 오류가 발생했습니다.");

    private HttpStatus httpStatus;
    private int code;
    private String message;
}
