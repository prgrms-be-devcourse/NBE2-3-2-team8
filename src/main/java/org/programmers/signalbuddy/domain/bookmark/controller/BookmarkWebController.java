package org.programmers.signalbuddy.domain.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.service.BookmarkService;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("bookmarks")
@RequiredArgsConstructor
public class BookmarkWebController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ModelAndView members(@PageableDefault(page = 0, size = 5) Pageable pageable, ModelAndView mv,
        User user) {
        user.setName("1");
        final Page<BookmarkResponse> pagedBookmarks = bookmarkService.findPagedBookmarks(pageable,
            user);
        mv.addObject("pagination", pagedBookmarks);
        mv.setViewName("/member/bookmarks");
        return mv;
    }
}
