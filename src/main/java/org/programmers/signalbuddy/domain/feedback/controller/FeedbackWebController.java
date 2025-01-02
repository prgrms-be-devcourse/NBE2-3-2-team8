package org.programmers.signalbuddy.domain.feedback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.comment.dto.CommentResponse;
import org.programmers.signalbuddy.domain.comment.service.CommentService;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.service.FeedbackService;
import org.programmers.signalbuddy.global.annotation.CurrentUser;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.dto.PageResponse;
import org.springframework.data.domain.PageRequest;
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

@Controller
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackWebController {

    private final FeedbackService feedbackService;
    private final CommentService commentService;

    @GetMapping
    public ModelAndView searchFeedbackList(@PageableDefault(page = 0, size = 10) Pageable pageable,
        @RequestParam(required = false, name = "answerStatus", defaultValue = "-1") Long answerStatus,
        @CurrentUser CustomUser2Member user,
        ModelAndView mv) {
        PageResponse<FeedbackResponse> response = feedbackService.searchFeedbackList(pageable,
            answerStatus);
        mv.setViewName("feedback/list");
        mv.addObject("response", response);
        mv.addObject("answerStatus", answerStatus);
        mv.addObject("user", user);
        return mv;
    }

    @GetMapping("/{feedbackId}")
    public ModelAndView searchFeedbackDetail(@PathVariable("feedbackId") Long feedbackId,
        @CurrentUser CustomUser2Member user,
        ModelAndView mv) {
        FeedbackResponse feedback = feedbackService.searchFeedbackDetail(feedbackId);
        PageResponse<CommentResponse> commentPage = commentService.searchCommentList(feedbackId,
            PageRequest.of(0, 1000));   // 페이지네이션 없이 모든 댓글을 가져오기 위함

        mv.setViewName("feedback/info");
        mv.addObject("feedback", feedback);
        mv.addObject("commentPage", commentPage);
        mv.addObject("user", user);
        return mv;
    }

    @GetMapping("/write")
    public ModelAndView showWriteFeedbackPage(
        ModelAndView mv) {
        mv.setViewName("feedback/write");
        mv.addObject("request", new FeedbackWriteRequest());
        return mv;
    }

    @PostMapping("/write")
    public ModelAndView writeFeedback(
        @ModelAttribute @Valid FeedbackWriteRequest feedbackWriteRequest,
        @CurrentUser CustomUser2Member user,
        ModelAndView mv) {
        feedbackService.writeFeedback(feedbackWriteRequest, user);
        mv.setViewName("redirect:/feedbacks");
        return mv;
    }

    @GetMapping("/edit/{feedbackId}")
    public ModelAndView showEditFeedbackPage(@PathVariable("feedbackId") Long feedbackId,
        ModelAndView mv) {
        FeedbackResponse feedback = feedbackService.searchFeedbackDetail(feedbackId);
        mv.setViewName("feedback/edit");
        mv.addObject("feedback", new FeedbackWriteRequest(feedback.getSubject(),
            feedback.getContent()));
        return mv;
    }
}
