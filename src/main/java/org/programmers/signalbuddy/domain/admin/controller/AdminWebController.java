package org.programmers.signalbuddy.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.admin.dto.AdminMemberResponse;
import org.programmers.signalbuddy.domain.admin.dto.WithdrawalMemberResponse;
import org.programmers.signalbuddy.domain.admin.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminWebController {

    private final AdminService adminService;

    @GetMapping()
    public ModelAndView adminsMain() {
        return new ModelAndView("admin/main");
    }

    @GetMapping("members/list")
    public ModelAndView getAllMembers(@PageableDefault(page = 0, size = 10, sort = "email") Pageable pageable, ModelAndView mv) {
        Page<AdminMemberResponse> members = adminService.getAllMembers(pageable);
        mv.setViewName("admin/list");
        mv.addObject("members", members);
        return mv;
    }

    @GetMapping("members-detail/{id}")
    public ModelAndView getMember(@PathVariable Long id, ModelAndView mv) {
        final AdminMemberResponse member = adminService.getMember(id);
        mv.setViewName("admin/detail");
        mv.addObject("member", member);
        return mv;
    }

    @GetMapping("members-withdrawal")
    public ModelAndView getAllWithdrawalMembers(@PageableDefault(page = 0, size = 10, sort = "email") Pageable pageable, ModelAndView mv) {
        Page<WithdrawalMemberResponse> members = adminService.getAllWithdrawalMembers(pageable);
        mv.setViewName("admin/withdrawal");
        mv.addObject("members", members);
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView loginForm(ModelAndView mv) {
        mv.setViewName("admin/loginform");
        return mv;
    }
}
