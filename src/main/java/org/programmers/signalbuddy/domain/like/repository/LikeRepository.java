package org.programmers.signalbuddy.domain.like.repository;

import org.programmers.signalbuddy.domain.feedback.entity.Feedback;
import org.programmers.signalbuddy.domain.like.entity.Like;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteByMemberAndFeedback(Member member, Feedback feedback);

    @Query("SELECT CASE  WHEN count(*)> 0 THEN true ELSE false END "
        + "FROM likes l "
        + "WHERE l.member.memberId = :memberId AND l.feedback.feedbackId = :feedbackId")
    boolean existsByMemberAndFeedback(@Param("memberId") Long memberId,
        @Param("feedbackId") Long feedbackId);
}
