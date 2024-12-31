package org.programmers.signalbuddy.domain.bookmark.repository;

import java.util.List;
import org.programmers.signalbuddy.domain.bookmark.dto.AdminBookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkRepositoryCustom {

    Page<BookmarkResponse> findPagedByMember(Pageable pageable, Long memberId);

    List<AdminBookmarkResponse> findBookmarkByMember(Long memberId);
}
