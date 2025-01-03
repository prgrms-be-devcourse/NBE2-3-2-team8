package org.programmers.signalbuddy.domain.crossroad.dto;

import org.programmers.signalbuddy.domain.crossroad.entity.Crossroad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import static org.programmers.signalbuddy.domain.crossroad.service.PointUtil.toPoint;

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

    public CrossroadApiResponse(Crossroad crossroad) {
        this.crossroadApiId = crossroad.getCrossroadApiId();
        this.name = crossroad.getName();
        this.lat = crossroad.getCoordinate().getX();
        this.lng = crossroad.getCoordinate().getY();
    }
}
