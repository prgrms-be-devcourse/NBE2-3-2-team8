package org.programmers.signalbuddy.domain.admin.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.signalbuddy.domain.bookmark.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AdminMemberResponse {

    private Long memberId;

    private String email;

    private String nickname;

    private String profileImageUrl;

    private MemberRole role;

    private MemberStatus memberStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String userAddress;

    private int bookmarkCount;

    private List<AdminBookmarkResponse> bookmarkResponses;

}
