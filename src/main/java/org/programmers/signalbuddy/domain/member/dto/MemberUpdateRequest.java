package org.programmers.signalbuddy.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberUpdateRequest {

    @Email(message = "이메일이 유효하지 않습니다.")
    @Schema(description = "이메일", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "udpate@example.com")
    private String email;

    @Schema(description = "비밀번호", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "password123")
    private String password;

    @Schema(description = "닉네임", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "Nickname")
    private String nickname;

    @Schema(description = "프로필 사진 파일", requiredMode = RequiredMode.NOT_REQUIRED, defaultValue = "profile-img.png")
    private MultipartFile imageFile;
}
