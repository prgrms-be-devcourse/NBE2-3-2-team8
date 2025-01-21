package org.programmers.signalbuddy.domain.crossroad.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadApiResponse;
import org.programmers.signalbuddy.domain.crossroad.entity.Crossroad;
import org.programmers.signalbuddy.domain.crossroad.entity.QCrossroad;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryCrossroadRepositoryImpl implements QueryCrossroadRepository {

    private final QCrossroad qCrossroad = QCrossroad.crossroad;

    private final JPAQueryFactory jqf;

    @Override
    public List<CrossroadApiResponse> findNearByCrossroads(double latitude, double longitude) {

        List<CrossroadApiResponse> res = new ArrayList<>();
        // ST_Distance_Sphere SQL 함수 사용
        String distanceExpression = "ST_Distance_Sphere(POINT({0}, {1}), POINT(lng, lat))";

        List<Crossroad> nearCrossroads = jqf.selectFrom(qCrossroad)
                .where(Expressions.numberTemplate(Double.class, distanceExpression, longitude, latitude).loe(50)) // 거리 조건
                .fetch();

        for(Crossroad near : nearCrossroads){
            res.add(new CrossroadApiResponse(near));
        }

        return res;
    }

/*
    SELECT station,
    ST_Distance_Sphere(@location, POINT(longitude, latitude) AS distance
    FROM Subway
    WHERE station = "신촌역";
*/

}
