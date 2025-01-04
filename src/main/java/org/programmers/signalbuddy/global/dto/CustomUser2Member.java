package org.programmers.signalbuddy.global.dto;

import lombok.Getter;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.global.security.basic.CustomUserDetails;
import org.programmers.signalbuddy.global.security.oauth.CustomOAuth2User;

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

    public CustomUser2Member(CustomOAuth2User customOAuth2User) {
        this.memberId = customOAuth2User.getMemberId();
        this.email = customOAuth2User.getEmail();
        this.profileImageUrl = customOAuth2User.getProfileImageUrl();
        this.nickname = customOAuth2User.getNickname();
        this.role = customOAuth2User.getRole();
        this.status = customOAuth2User.getStatus();
    }

    public CustomUser2Member(String arg) {}
}
