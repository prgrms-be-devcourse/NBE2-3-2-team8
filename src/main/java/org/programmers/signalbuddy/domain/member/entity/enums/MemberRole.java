package org.programmers.signalbuddy.domain.member.entity.enums;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("관리자"), USER("사용자");

    private final String role;

    MemberRole(String role) {
        this.role = role;
    }
}
