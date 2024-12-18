package org.programmers.signalbuddy.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.member.entity.dto.MemberResponse;
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

    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        final MemberResponse member = memberService.getMember(id);
        return ResponseEntity.ok(member);
    }
}
