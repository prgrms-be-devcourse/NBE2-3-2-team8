package org.programmers.signalbuddy.domain.member.controller;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.member.entity.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.service.AdminMemberService;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Member API")
public class MemberController {

    private final MemberService memberService;
    private final AdminMemberService adminMemberService;


    @Operation(summary = "사용자 조회 API")
    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        final MemberResponse member = memberService.getMember(id);
        return ResponseEntity.ok(member);
    }

    @Operation(summary = "사용자 수정 API")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @PatchMapping("{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
        @Validated @RequestBody MemberUpdateRequest memberUpdateRequest) {
        // TODO : 사용자 이미지 저장 방식 정해지면 수정 필요
        log.info("id : {}, UpdateRequest: {}", id, memberUpdateRequest);
        final MemberResponse updated = memberService.updateMember(id, memberUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @Operation(summary = "사용자 탈퇴 API")
    @ApiResponse(responseCode = "200", description = "탈퇴 성공")
    @DeleteMapping("{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        log.info("id : {}", id);
        final MemberResponse deleted = memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<AdminMemberResponse>> getMembers() {
        List<AdminMemberResponse> members = adminMemberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
}
