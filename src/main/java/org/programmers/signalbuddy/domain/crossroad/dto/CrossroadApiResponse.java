package org.programmers.signalbuddy.domain.crossroad.dto;

import lombok.NoArgsConstructor;
import org.programmers.signalbuddy.domain.crossroad.entity.Crossroad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;
import org.programmers.signalbuddy.domain.crossroad.service.PointUtil;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public Point toPoint() {
        return PointUtil.toPoint(this.lat, this.lng);
    }

    public CrossroadApiResponse(Crossroad crossroad) {
        this.crossroadApiId = crossroad.getCrossroadApiId();
        this.name = crossroad.getName();
        this.lat = crossroad.getCoordinate().getY();
        this.lng = crossroad.getCoordinate().getX();
    }
}
