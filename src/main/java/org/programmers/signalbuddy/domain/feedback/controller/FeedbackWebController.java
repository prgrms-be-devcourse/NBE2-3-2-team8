package org.programmers.signalbuddy.domain.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.service.FeedbackService;
import org.programmers.signalbuddy.global.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackWebController {

    private final FeedbackService feedbackService;

    @GetMapping
    public ModelAndView searchFeedbackList(
        @PageableDefault(page = 0, size = 10) Pageable pageable,
        @RequestParam(required = false, name = "answerStatus", defaultValue = "-1") Long answerStatus,
        ModelAndView mv) {
        PageResponse<FeedbackResponse> response = feedbackService.searchFeedbackList(pageable, answerStatus);
        mv.setViewName("feedback/list");
        mv.addObject("response", response);
        mv.addObject("answerStatus", answerStatus);
        return mv;
    }

    @GetMapping("/{feedbackId}")
    public ModelAndView searchFeedbackDetail(
        @PathVariable("feedbackId") Long feedbackId,
        ModelAndView mv) {
        FeedbackResponse feedback = feedbackService.searchFeedbackDetail(feedbackId);
        mv.setViewName("feedback/info");
        mv.addObject("feedback", feedback);
        return mv;
    }
}
