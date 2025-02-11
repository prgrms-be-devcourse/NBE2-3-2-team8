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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder(builderMethodName = "create")
    private Feedback(final String subject, final String content, final Member member) {
        this.subject = Objects.requireNonNull(subject);
        this.content = Objects.requireNonNull(content);
        this.likeCount = 0L;
        this.answerStatus = AnswerStatus.BEFORE;
        this.member = Objects.requireNonNull(member);
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

    public void increaseLike() {
        this.likeCount += 1;
    }

    public void decreaseLike() {
        if (this.likeCount > 0) {
            this.likeCount -= 1;
        }
    }
}