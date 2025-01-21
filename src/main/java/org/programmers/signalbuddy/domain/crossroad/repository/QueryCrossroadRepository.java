package org.programmers.signalbuddy.domain.crossroad.repository;

import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadApiResponse;

import java.util.List;

public interface QueryCrossroadRepository {
    List<CrossroadApiResponse> findNearByCrossroads(double latitude, double longitude);
}
