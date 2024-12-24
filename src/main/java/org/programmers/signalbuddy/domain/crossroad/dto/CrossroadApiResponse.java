package org.programmers.signalbuddy.domain.crossroad.dto;

import static org.programmers.signalbuddy.domain.crossroad.service.PointUtil.toPoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CrossroadApiResponse {

    @JsonProperty("itstId")
    private String crossroadApiId;

    @JsonProperty("itstNm")
    private String name;

    @JsonProperty("mapCtptIntLat")
    private Double lat; // 위도

    @JsonProperty("mapCtptIntLot")
    private Double lng; // 경도

    public Point getPoint() {
        return toPoint(this.lat, this.lng);
    }
}
