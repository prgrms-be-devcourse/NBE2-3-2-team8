package org.programmers.signalbuddy.domain.member;

import lombok.Getter;

@Getter
public enum MemberStatus {
    ACTIVITY("활성"), WITHDRAWAL("탈퇴");

    private final String status;

    MemberStatus(String status) {
        this.status = status;
    }
}
