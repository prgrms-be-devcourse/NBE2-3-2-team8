package org.programmers.signalbuddy.domain.crossroad.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.crossroad.service.CrossroadService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/crossroads")
@RequiredArgsConstructor
public class CrossroadController {

    private final CrossroadService crossroadService;

    @PostMapping("/save")
    public ResponseEntity<Void> saveCrossroadDates(@Min(1) @RequestParam("page") int page,
        @Min(10) @RequestParam("size") int pageSize) {
        crossroadService.saveCrossroadDates(page, pageSize);
        return ResponseEntity.ok().build();
    }
}
