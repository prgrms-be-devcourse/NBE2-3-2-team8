package org.programmers.signalbuddy.domain.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.service.BookmarkService;
import org.programmers.signalbuddy.global.annotation.CurrentUser;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("bookmarks")
@RequiredArgsConstructor
public class BookmarkWebController {

    private final BookmarkService bookmarkService;

    @ModelAttribute("user")
    public CustomUser2Member currentUser(@CurrentUser CustomUser2Member user) {
        return user;
    }

    @GetMapping("add")
    public ModelAndView addBookmark(ModelAndView mv) {
        mv.setViewName("/member/bookmark/add");
        return mv;
    }

    @GetMapping("{id}/edit")
    public ModelAndView getBookmark(@PathVariable Long id, ModelAndView mv) {
        final BookmarkResponse bookmark = bookmarkService.getBookmark(id);
        mv.addObject("bookmark", bookmark);
        mv.setViewName("/member/bookmark/edit");
        return mv;
    }
}
