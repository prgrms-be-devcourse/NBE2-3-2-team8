package org.programmers.signalbuddy.global.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HttpRequestManager {

    private final MeterRegistry meterRegistry;

    public void increase(Long signalId) {
        String id = String.valueOf(signalId);
        Counter.builder("crossroad.call")
            .tag("class", this.getClass().getName())
            .tag("method", "getCrossRoadCall")
            .tag("id", String.valueOf(id))
            .description("교차로 호출 횟수")
            .register(meterRegistry)
            .increment();
    }
}
