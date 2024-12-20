package org.programmers.signalbuddy.domain.bookmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkRequest;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.service.BookmarkService;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/bookmarks")
@Tag(name = "Bookmark API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "즐겨찾기 목록 조회", description = "Pagination")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "즐겨찾기 목록 조회 성공")
    public ResponseEntity<Page<BookmarkResponse>> getBookmarks(
        @PageableDefault(page = 0, size = 5) Pageable pageable, User user) {
        final Page<BookmarkResponse> bookmarks = bookmarkService.findPagedBookmarks(pageable, user);
        return ResponseEntity.ok().body(bookmarks);
    }

    @Operation(summary = "즐겨찾기 등록", description = "즐겨찾기 등록 성공")
    @PostMapping
    @ApiResponse(responseCode = "201", description = "즐겨찾기 등록 성공")
    public ResponseEntity<BookmarkResponse> addBookmark(
        @RequestBody @Validated BookmarkRequest createRequest, User user) {
        log.info("createRequest: {}", createRequest);
        final BookmarkResponse response = bookmarkService.createBookmark(createRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
