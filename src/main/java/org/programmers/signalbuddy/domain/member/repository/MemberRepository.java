package org.programmers.signalbuddy.domain.member.repository;

import java.util.Optional;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {

    Page<Member> findAll(Pageable pageable);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Member save(Member member);

    void delete(Member member);

    default Member findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
