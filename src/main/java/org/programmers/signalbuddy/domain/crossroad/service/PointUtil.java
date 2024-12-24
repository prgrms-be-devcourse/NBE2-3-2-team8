package org.programmers.signalbuddy.domain.crossroad.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public final class PointUtil {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * Double 형 위도 경도 값을 Point 객체로 만듦
     * @param lat 위도
     * @param lng 경도
     * @return DB에 저장시킬 수 있는 Point 타입 (x: 경도, y: 위도)
     */
    public static Point toPoint(Double lat, Double lng) {
        return geometryFactory.createPoint(new Coordinate(lng, lat));
    }
}
