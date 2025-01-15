package org.programmers.signalbuddy.domain.like.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LikeRequestType {

    ADD("추가 요청"), CANCEL("취소 요청");

    private final String requestType;
}
