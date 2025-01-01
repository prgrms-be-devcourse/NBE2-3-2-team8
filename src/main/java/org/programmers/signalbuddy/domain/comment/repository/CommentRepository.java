package org.programmers.signalbuddy.domain.comment.repository;

import org.programmers.signalbuddy.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    @Modifying
    @Query("DELETE FROM comments c WHERE c.feedback.feedbackId = :feedbackId")
    void deleteAllByFeedbackId(@Param("feedbackId") Long feedbackId);
}
