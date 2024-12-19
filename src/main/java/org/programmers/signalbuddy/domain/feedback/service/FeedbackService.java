package org.programmers.signalbuddy.domain.feedback.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackMapper;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackResponse;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.feedback.repository.FeedbackRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MemberRepository memberRepository;

    // TODO: 인자값에 User 객체는 나중에 변경해야 함!
    @Transactional
    public FeedbackResponse writeFeedback(FeedbackWriteRequest request, User user) {
        Member member = memberRepository.findById(Long.parseLong(user.getName()))
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));

        Feedback feedback = Feedback.create(request, member);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        return FeedbackMapper.INSTANCE.toResponse(savedFeedback);
    }
}
