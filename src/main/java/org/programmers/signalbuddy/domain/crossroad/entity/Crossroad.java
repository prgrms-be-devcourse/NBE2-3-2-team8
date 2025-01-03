package org.programmers.signalbuddy.domain.crossroad.entity;

import org.programmers.signalbuddy.domain.basetime.BaseTimeEntity;
import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadApiResponse;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity(name = "crossroads")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Crossroad extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crossroadId;

    @Column(nullable = false, unique = true)
    private String crossroadApiId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Point coordinate;

    public Crossroad(CrossroadApiResponse response) {
        this.crossroadApiId = response.getCrossroadApiId();
        this.name = response.getName();
        this.coordinate = response.getPoint();
    }

}