package org.programmers.signalbuddy.domain.feedback.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.programmers.signalbuddy.domain.member.entity.Member;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackResponse {

    private Long feedbackId;
    private String subject;
    private String content;
    private Long likeCount;
    private Member member;
}
