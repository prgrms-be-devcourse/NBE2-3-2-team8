package org.programmers.signalbuddy.domain.crossroad.service;

import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadApiResponse;
import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadStateApiResponse;
import org.programmers.signalbuddy.domain.crossroad.exception.CrossroadErrorCode;
import org.programmers.signalbuddy.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrossroadProvider {

    @Value("${t-data-api.api-key}")
    private String API_KEY;
    @Value("${t-data-api.crossroad-api}")
    private String CROSSROAD_API_URL;
    @Value("${t-data-api.traffic-light-api}")
    private String SIGNAL_STATE_URL;

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
                log.error("{}\n", e.getMessage(), e.getCause());
                throw new BusinessException(CrossroadErrorCode.CROSSROAD_API_REQUEST_FAILED);
            })
            .block();
    }

    public List<CrossroadStateApiResponse> requestCrossroadStateApi(Long id) {
        return webClient.get()
            .uri(SIGNAL_STATE_URL,
                uriBuilder -> uriBuilder
                        .queryParam("apiKey",API_KEY)
                        .queryParam("itstId",id)
                        .queryParam("pageNo",1)
                        .queryParam("numOfRows",1)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CrossroadStateApiResponse>>() {})
                .onErrorMap(e->{
                    log.error("{}\n", e.getMessage(), e.getCause());
                    throw new BusinessException(CrossroadErrorCode.CROSSROAD_API_REQUEST_FAILED);
                })
                .block();
    }
}
