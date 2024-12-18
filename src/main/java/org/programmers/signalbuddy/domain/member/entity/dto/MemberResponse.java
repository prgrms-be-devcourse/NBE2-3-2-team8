package org.programmers.signalbuddy.domain.member.entity.dto;

import org.programmers.signalbuddy.domain.member.MemberStatus;

public class MemberResponse {

    private Long memberId;

    private String email;

    private String nickname;

    private String profileImageUrl;

    private String role;

    private MemberStatus memberStatus;
}
