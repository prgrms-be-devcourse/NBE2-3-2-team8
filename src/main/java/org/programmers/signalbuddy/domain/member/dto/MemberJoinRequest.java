package org.programmers.signalbuddy.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequest {

    @Schema(description = "프로필 사진", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "/images/member/profile-icon.png")
    private MultipartFile profileImageUrl;

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Schema(description = "이메일", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "udpate@example.com")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
    @Schema(description = "닉네임", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "Nickname")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Schema(description = "비밀번호", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "password123")
    private String password;
}
