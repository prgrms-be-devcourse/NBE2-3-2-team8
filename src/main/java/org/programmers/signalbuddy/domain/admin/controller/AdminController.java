package org.programmers.signalbuddy.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.admin.service.AdminService;
import org.programmers.signalbuddy.domain.admin.dto.AdminMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admins")
@RequiredArgsConstructor
@Tag(name="Admin API")
public class AdminController {

    private final AdminService adminService;


    @Operation(summary = "사용자 전체 조회 API")
    @GetMapping("/members")
    public ResponseEntity<Page<AdminMemberResponse>> getAdminMembers(
        @PageableDefault(page = 0, size = 10, sort = "email") Pageable pageable) {

        Page<AdminMemberResponse> members = adminService.getAllMembers(pageable);
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "사용자 상세 조회 API")
    @GetMapping("members/{id}")
    public ResponseEntity<AdminMemberResponse> getAdminMember(@PathVariable Long id) {
        final AdminMemberResponse member = adminService.getMember(id);
        return ResponseEntity.ok(member);
    }
}
