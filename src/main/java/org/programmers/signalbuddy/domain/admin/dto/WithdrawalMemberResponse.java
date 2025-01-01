package org.programmers.signalbuddy.domain.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class WithdrawalMemberResponse {
    private Long memberId;

    private String email;

    private String nickname;

    private String profileImageUrl;

    private MemberRole role;

    private MemberStatus memberStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
