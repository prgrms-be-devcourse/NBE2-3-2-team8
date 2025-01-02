package org.programmers.signalbuddy.global.dto;

import lombok.Getter;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.global.security.CustomUserDetails;

@Getter
public class CustomUser2Member {

    private Long memberId;
    private String email;
    private String profileImageUrl;
    private String nickname;
    private MemberRole role;
    private MemberStatus status;

    public CustomUser2Member(CustomUserDetails customUserDetails) {
        this.memberId = customUserDetails.getMemberId();
        this.email = customUserDetails.getEmail();
        this.profileImageUrl = customUserDetails.getProfileImageUrl();
        this.nickname = customUserDetails.getNickname();
        this.role = customUserDetails.getRole();
        this.status = customUserDetails.getStatus();
    }

    public CustomUser2Member(String arg) {}
}
