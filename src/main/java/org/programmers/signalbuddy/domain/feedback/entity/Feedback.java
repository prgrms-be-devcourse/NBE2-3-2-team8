package org.programmers.signalbuddy.domain.feedback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.programmers.signalbuddy.domain.basetime.BaseTimeEntity;
import org.programmers.signalbuddy.domain.feedback.dto.FeedbackWriteRequest;
import org.programmers.signalbuddy.domain.feedback.entity.enums.AnswerStatus;
import org.programmers.signalbuddy.domain.member.entity.Member;

@Entity(name = "feedbacks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerStatus answerStatus;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Feedback(FeedbackWriteRequest request, Member member) {
        this.subject = request.getSubject();
        this.content = request.getContent();
        this.likeCount = 0L;
        this.answerStatus = AnswerStatus.BEFORE;
        this.member = member;
    }

    public static Feedback create(FeedbackWriteRequest request, Member member) {
        return new Feedback(request, member);
    }

    public void updateFeedback(FeedbackWriteRequest request) {
        if (!this.subject.equals(request.getSubject())) {
            this.subject = request.getSubject();
        }

        if (!this.content.equals(request.getContent())) {
            this.content = request.getContent();
        }
    }

    public void updateFeedbackStatus() {
        if (AnswerStatus.BEFORE.equals(this.getAnswerStatus())) {
            this.answerStatus = AnswerStatus.COMPLETION;
        } else if (AnswerStatus.COMPLETION.equals(this.getAnswerStatus())) {
            this.answerStatus = AnswerStatus.BEFORE;
        }
    }
}