package org.programmers.signalbuddy.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geolatte.geom.BufferAccessException;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.service.BookmarkService;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.service.FeedbackService;
import org.programmers.signalbuddy.domain.member.dto.MemberJoinRequest;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.service.MemberService;
import org.programmers.signalbuddy.global.annotation.CurrentUser;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberWebController {

    private final BookmarkService bookmarkService;
    private final FeedbackService feedbackService;
    private final MemberService memberService;

    @ModelAttribute("user")
    public CustomUser2Member currentUser(@CurrentUser CustomUser2Member user) {
        return user;
    }

    @GetMapping
    public ModelAndView getMemberView(ModelAndView mv) {
        mv.setViewName("member/info");
        return mv;
    }

    @GetMapping("edit")
    public ModelAndView editMemberView(ModelAndView mv) {
        mv.setViewName("member/edit");
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView loginForm(@RequestParam(required = false) String error, ModelAndView mv) {

        if(error != null) {
            mv.addObject("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
        }
        mv.setViewName("member/loginform");
        return mv;
    }

    @GetMapping("{id}/bookmarks")
    public ModelAndView findPagedBookmarks(@PageableDefault(page = 0, size = 5) Pageable pageable,
        ModelAndView mv, @PathVariable Long id) {
        final Page<BookmarkResponse> pagedBookmarks = bookmarkService.findPagedBookmarks(pageable,
            id);
        mv.addObject("pagination", pagedBookmarks);
        mv.setViewName("/member/bookmark/list");
        return mv;
    }

    @GetMapping("{id}/feedbacks")
    public ModelAndView findPagedFeedbacks(@PageableDefault(page = 0, size = 5) Pageable pageable,
        ModelAndView mv, @PathVariable Long id) {
        final Page<FeedbackResponse> pagedFeedbacks = feedbackService.findPagedFeedbacksByMember(id,
            pageable);
        mv.addObject("pagination", pagedFeedbacks);
        mv.setViewName("member/feedback/list");
        return mv;
    }

    @GetMapping("/signup")
    public ModelAndView signup(ModelAndView mv) {

        mv.addObject("memberJoinRequest", new MemberJoinRequest());
        mv.setViewName("member/signup");
        return mv;
    }

    @PostMapping("/signup")
    public ModelAndView registerMember(@ModelAttribute @Valid MemberJoinRequest joinMember,
        ModelAndView mv) {

        try {
            memberService.joinMember(joinMember);
            mv.setViewName("redirect:/members/login");
        } catch (BusinessException e) {
            if (e.getErrorCode().equals(MemberErrorCode.ALREADY_EXIST_EMAIL)) {
                mv.addObject("errorMessage", e.getMessage());
            }
            mv.setViewName("member/signup");
        }
        return mv;
    }
}

