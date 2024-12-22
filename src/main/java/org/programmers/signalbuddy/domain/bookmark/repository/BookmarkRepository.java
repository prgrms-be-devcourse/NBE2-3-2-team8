package org.programmers.signalbuddy.domain.bookmark.repository;

import java.util.List;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>,

    BookmarkRepositoryCustom {
    List<Bookmark> findAllByMember_MemberId(Long id);
}
