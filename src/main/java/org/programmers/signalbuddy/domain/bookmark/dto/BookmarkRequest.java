package org.programmers.signalbuddy.domain.bookmark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class BookmarkRequest {

    @Min(value = -180, message = "경도는 -180 이상이어야 합니다.")
    @Max(value = 180, message = "경도는 180 이하여야 합니다.")
    @Schema(description = "경도", example = "126.9779451")
    private double lng;

    @Min(value = -90, message = "위도는 -90 이상이어야 합니다.")
    @Max(value = 90, message = "위도는 90 이하여야 합니다.")
    @Schema(description = "위도", example = "37.5662952")
    private double lat;

    @NotBlank(message = "주소를 입력해주세요.")
    @Schema(description = "주소", example = "서울시청")
    private String address;
}
