package org.programmers.signalbuddy.domain.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.member.entity.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.service.AdminMemberService;
import org.programmers.signalbuddy.domain.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AdminMemberService adminMemberService;

    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        final MemberResponse member = memberService.getMember(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<AdminMemberResponse>> getMembers() {
        List<AdminMemberResponse> members = adminMemberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

}
