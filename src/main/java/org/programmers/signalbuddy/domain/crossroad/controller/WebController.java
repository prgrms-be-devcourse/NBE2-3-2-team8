package org.programmers.signalbuddy.domain.crossroad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class WebController {

    @GetMapping
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("main");
        return mv;
    }
}
