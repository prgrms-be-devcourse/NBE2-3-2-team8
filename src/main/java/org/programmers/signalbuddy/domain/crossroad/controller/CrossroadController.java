package org.programmers.signalbuddy.domain.crossroad.controller;

import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadApiResponse;
import org.programmers.signalbuddy.domain.crossroad.dto.CrossroadStateApiResponse;
import org.programmers.signalbuddy.domain.crossroad.repository.CrossroadRepository;
import org.programmers.signalbuddy.domain.crossroad.service.CrossroadService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/crossroads")
@RequiredArgsConstructor
public class CrossroadController {

    private final CrossroadService crossroadService;
    private final CrossroadRepository crossroadRepository;

    @PostMapping("/save")
    public ResponseEntity<Void> saveCrossroadDates(@Min(1) @RequestParam("page") int page,
        @Min(10) @RequestParam("size") int pageSize) {
        crossroadService.saveCrossroadDates(page, pageSize);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/marker") // 저장된 DB 데이터를 기반으로 map에 찍을 marker의 데이터를 point로 가져오기
    public ResponseEntity<List<CrossroadApiResponse>> pointToMarker(){
        List<CrossroadApiResponse> markers = crossroadService.getAllMarkers();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(markers);
    }


    @GetMapping("/state/{id}") // id를 기반으로 신호등 데이터 상태 검색
    public ResponseEntity<List<CrossroadStateApiResponse>> markerToState(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        List<CrossroadStateApiResponse> stateRes = crossroadService.checkSignalState(id);

        return  ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(stateRes);
    }

    @GetMapping("/around") // 좌표 값을 기반으로 50m이내 신호등 반환
    public ResponseEntity<List<CrossroadApiResponse>> aroundCrossroad(
            @RequestParam double lat,
            @RequestParam double lng
    ) {

        List<CrossroadApiResponse> aroundSign = crossroadRepository.findNearByCrossroads(lat, lng);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aroundSign);

    }


}
