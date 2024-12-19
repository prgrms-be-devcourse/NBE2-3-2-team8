package org.programmers.signalbuddy.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "사용자 조회 API")
    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        final MemberResponse member = memberService.getMember(id);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "사용자 수정 API")
    @ApiResponse(responseCode = "201", description = "수정 성공")
    @PatchMapping("{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
        @Validated @RequestBody MemberUpdateRequest memberUpdateRequest) {
        log.info("id : {}, UpdateRequest: {}", id, memberUpdateRequest);
        final MemberResponse updated = memberService.updateMember(id, memberUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

}