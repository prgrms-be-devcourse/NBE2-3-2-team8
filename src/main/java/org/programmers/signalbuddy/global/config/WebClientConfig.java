package org.programmers.signalbuddy.global.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    @Value("${t-data-api.base-url}")
    private String BASE_URL;

    private final int processors = Runtime.getRuntime().availableProcessors();    // PC의 Processor 개수
    private final HttpClient httpClient = HttpClient.create(
            ConnectionProvider.builder("ApiConnections")
                .maxConnections(processors * 2)
                .pendingAcquireTimeout(Duration.ofMillis(0))    // 커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
                .pendingAcquireMaxCount(-1) // 커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
                .maxIdleTime(Duration.ofMillis(1000L))  // 최대 유휴 시간 1초로 설정
                .evictInBackground(Duration.ofMillis(1000L)) // 1초마다 유휴 Connections 확인하고 제거
                .build()).responseTimeout(Duration.ofSeconds(30));    // 응답 초과 시간 30초로 설정

    @Bean
    public WebClient seoulApiWebClient() {

        return WebClient.builder()
            .baseUrl(BASE_URL)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
