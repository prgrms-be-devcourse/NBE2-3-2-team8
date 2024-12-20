package org.programmers.signalbuddy.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public Page<BookmarkResponse> findPagedBookmarks(Pageable pageable, User user) {
        // TODO : 2번째 인자 memberId 수정 필요
        return bookmarkRepository.findPagedByMember(pageable, 1L);
    }
}
