package org.programmers.signalbuddy.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.member.dto.MemberResponse;
import org.programmers.signalbuddy.domain.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberWebController {

    private final MemberService memberService;

    @GetMapping
    public ModelAndView info(ModelAndView mv) {
        mv.setViewName("member/info");
        // TODO : 수정 필요
        final MemberResponse member = memberService.getMember(1L);
        mv.addObject("member", member);
        return mv;
    }

    @GetMapping("edit")
    public ModelAndView infoView(ModelAndView mv) {
        mv.setViewName("member/edit");
        // TODO : 수정 필요
        final MemberResponse member = memberService.getMember(1L);
        mv.addObject("member", member);
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView loginForm(ModelAndView mv) {
        mv.setViewName("member/loginform");
        return mv;
    }
}

