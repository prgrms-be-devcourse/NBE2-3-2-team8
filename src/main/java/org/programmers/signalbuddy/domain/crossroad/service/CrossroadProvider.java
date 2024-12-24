package org.programmers.signalbuddy.domain.crossroad.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadApiResponse;
import org.programmers.signalbuddy.domain.crossroad.exception.CrossroadErrorCode;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrossroadProvider {

    @Value("${t-data-api.api-key}")
    private String API_KEY;
    @Value("${t-data-api.crossroad-api}")
    private String CROSSROAD_API_URL;

    private final WebClient webClient;

    public List<CrossroadApiResponse> requestCrossroadApi(int page, int pageSize) {
        return webClient.get()
            .uri(CROSSROAD_API_URL,
                uriBuilder -> uriBuilder
                    .queryParam("apiKey", API_KEY)
                    .queryParam("pageNo", page)
                    .queryParam("numOfRows", pageSize)
                    .build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<CrossroadApiResponse>>() {})
            .onErrorMap(e -> {
                log.error("{}\n{}", e.getMessage(), e.getCause());
                throw new BusinessException(CrossroadErrorCode.CROSSROAD_API_REQUEST_FAILED);
            })
            .block();
    }
}
