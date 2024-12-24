package org.programmers.signalbuddy.domain.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("members")
public class MemberWebController {

    @GetMapping
    public ModelAndView info(ModelAndView mv) {
        mv.setViewName("member/info");
        // TODO : 미구현...
        return mv;
    }
}

