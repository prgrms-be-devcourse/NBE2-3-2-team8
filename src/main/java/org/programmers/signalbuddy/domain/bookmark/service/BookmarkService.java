package org.programmers.signalbuddy.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
}
