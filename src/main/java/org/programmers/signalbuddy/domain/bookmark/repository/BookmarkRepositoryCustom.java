package org.programmers.signalbuddy.domain.bookmark.repository;

import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkRepositoryCustom {

    Page<BookmarkResponse> findPagedByMember(Pageable pageable, Long memberId);
}
