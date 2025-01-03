package org.programmers.signalbuddy.domain.crossroad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/crossroad")
public class WebController {

    @GetMapping("/main")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/crossroad/main");
        return mv;
    }
}
