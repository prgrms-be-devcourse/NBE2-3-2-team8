package org.programmers.signalbuddy.domain.member.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.signalbuddy.domain.bookmark.entity.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.member.MemberRole;
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

    private List<AdminBookmarkResponse> bookmarkResponses;

}
