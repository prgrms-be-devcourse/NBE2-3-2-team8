package org.programmers.signalbuddy.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class MemberResponse {


    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "회원 이메일", example = "test@example.com")
    private String email;

    @Schema(description = "회원 닉네임", example = "TestUser")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "http://example.com/profile.jpg")
    private String profileImageUrl;

    @Schema(description = "회원 역할", example = "USER")
    private String role;

    @Schema(description = "회원 상태", exampleClasses = MemberStatus.class)
    private MemberStatus memberStatus;
}
