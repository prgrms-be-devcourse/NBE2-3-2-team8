package org.programmers.signalbuddy.domain.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkRequest;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.bookmark.exception.BookmarkErrorCode;
import org.programmers.signalbuddy.domain.bookmark.mapper.BookmarkMapper;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.exception.MemberErrorCode;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.exception.BusinessException;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final GeometryFactory geometryFactory;
    private final MemberRepository memberRepository;

    public Page<BookmarkResponse> findPagedBookmarks(Pageable pageable, User user) {
        // TODO : 2번째 인자 memberId 수정 필요
        return bookmarkRepository.findPagedByMember(pageable, 1L);
    }

    public BookmarkResponse createBookmark(BookmarkRequest createRequest, User user) {
        final Member member = getMember(user);

        final Point point = toPoint(createRequest.getLng(), createRequest.getLat());

        final Bookmark bookmark = BookmarkMapper.INSTANCE.toEntity(createRequest, point, member);
        final Bookmark save = bookmarkRepository.save(bookmark);
        return BookmarkMapper.INSTANCE.toDto(save);
    }

    @Transactional
    public BookmarkResponse updateBookmark(BookmarkRequest updateRequest, Long id, User user) {
        final Member member = getMember(user);

        final Bookmark bookmark = bookmarkRepository.findById(id)
            .orElseThrow(() -> new BusinessException(BookmarkErrorCode.NOT_FOUND_BOOKMARK));

        final Point point = toPoint(updateRequest.getLng(), updateRequest.getLat());

        bookmark.update(point, updateRequest.getAddress());
        return BookmarkMapper.INSTANCE.toDto(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long id, User user) {
        final Member member = getMember(user);
        final Bookmark bookmark = bookmarkRepository.findById(id)
            .orElseThrow(() -> new BusinessException(BookmarkErrorCode.NOT_FOUND_BOOKMARK));
        bookmarkRepository.delete(bookmark);
    }

    private Member getMember(User user) {
        return memberRepository.findById(1L) // TODO : 1L -> memberId로 수정 필요
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private Point toPoint(double lng, double lat) {
        if (lng < -180 || lng > 180 || lat < -90 || lat > 90) {
            throw new BusinessException(BookmarkErrorCode.INVALID_COORDINATES);
        }
        return geometryFactory.createPoint(new Coordinate(lng, lat));
    }
}
