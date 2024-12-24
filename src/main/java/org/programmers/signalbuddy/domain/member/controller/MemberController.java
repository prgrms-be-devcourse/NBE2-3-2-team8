package org.programmers.signalbuddy.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.service.FeedbackService;
import org.programmers.signalbuddy.domain.member.dto.MemberJoinRequest;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.dto.MemberUpdateRequest;
import org.programmers.signalbuddy.domain.admin.service.AdminService;
import org.programmers.signalbuddy.domain.member.service.MemberService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final AdminService adminService;
    private final FeedbackService feedbackService;

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

    //TODO : 주석제거
    /*
    @Operation(summary = "관리자: 전체 사용자 조회 API")
    @GetMapping("admin")
    public ResponseEntity<Page<AdminMemberResponse>> getAdminMembers(
        @PageableDefault(page = 0, size = 10, sort = "email") Pageable pageable) {

        Page<AdminMemberResponse> members = adminMemberService.getAllMembers(pageable);
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "관리자 : 사용자 조회 API")
    @GetMapping("admin/{id}")
    public ResponseEntity<AdminMemberResponse> getAdminMember(@PathVariable Long id) {
        final AdminMemberResponse member = adminMemberService.getMember(id);
        return ResponseEntity.ok(member);
    }
*/
    @Operation(summary = "해당 사용자 피드백 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "피드백 목록 조회 성공")
    @GetMapping("{id}/feedbacks")
    public ResponseEntity<Page<FeedbackResponse>> getFeedbacks(@PathVariable Long id,
        @PageableDefault(page = 0, size = 10) Pageable pageable) {
        log.info("id : {}", id);
        final Page<FeedbackResponse> feedbacks = feedbackService.getFeedbacksByMemberId(id,
            pageable);
        return ResponseEntity.ok(feedbacks);
    }

    @Operation(summary = "사용자 회원 가입 API")
    @ApiResponse(responseCode = "200", description = "회원 가입 성공")
    @PostMapping("/join")
    public ResponseEntity<MemberResponse> joinMember(
        @Validated @RequestBody MemberJoinRequest memberJoinRequest) {

        log.info("memberJoinRequest: {}", memberJoinRequest);
        MemberResponse saved = memberService.joinMember(memberJoinRequest);
        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }

    @GetMapping("/files/{filename}")
    @Operation(summary = "이미지 조회 API", description = "페이지에서 이미지를 조회할 수 있는 API")
    @ApiResponse(responseCode = "200", description = "이미지 파일 리턴")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        final Resource image = memberService.getProfileImage(filename);
        return ResponseEntity.status(HttpStatus.OK).body(image);
    }
}
