package org.programmers.signalbuddy.domain.bookmark.service;

import java.util.Objects;
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
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.exception.BusinessException;
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

    public Page<BookmarkResponse> findPagedBookmarks(Pageable pageable, Long memberId) {
        return bookmarkRepository.findPagedByMember(pageable, memberId);
    }

    public BookmarkResponse createBookmark(BookmarkRequest createRequest, CustomUser2Member user) {
        final Member member = getMember(user);

        final Point point = toPoint(createRequest.getLng(), createRequest.getLat());

        final Bookmark bookmark = BookmarkMapper.INSTANCE.toEntity(createRequest, point, member);
        final Bookmark save = bookmarkRepository.save(bookmark);
        return BookmarkMapper.INSTANCE.toDto(save);
    }

    @Transactional
    public BookmarkResponse updateBookmark(BookmarkRequest updateRequest, Long id,
        CustomUser2Member user) {
        final Member member = getMember(user);

        final Bookmark bookmark = bookmarkRepository.findById(id)
            .orElseThrow(() -> new BusinessException(BookmarkErrorCode.NOT_FOUND_BOOKMARK));

        if (!Objects.equals(member.getMemberId(), bookmark.getMember().getMemberId())) {
            throw new BusinessException(BookmarkErrorCode.UNAUTHORIZED_MEMBER_ACCESS);
        }

        final Point point = toPoint(updateRequest.getLng(), updateRequest.getLat());

        bookmark.update(point, updateRequest.getAddress(), updateRequest.getName());
        return BookmarkMapper.INSTANCE.toDto(bookmark);
    }

    @Transactional
    public void deleteBookmark(Long id, CustomUser2Member user) {
        final Member member = getMember(user);

        final Bookmark bookmark = bookmarkRepository.findById(id)
            .orElseThrow(() -> new BusinessException(BookmarkErrorCode.NOT_FOUND_BOOKMARK));

        if (!Objects.equals(member.getMemberId(), bookmark.getMember().getMemberId())) {
            throw new BusinessException(BookmarkErrorCode.UNAUTHORIZED_MEMBER_ACCESS);
        }

        bookmarkRepository.delete(bookmark);
    }

    private Member getMember(CustomUser2Member user) {
        return memberRepository.findById(user.getMemberId())
            .orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
    }

    private Point toPoint(double lng, double lat) {
        if (lng < -180 || lng > 180 || lat < -90 || lat > 90) {
            throw new BusinessException(BookmarkErrorCode.INVALID_COORDINATES);
        }
        return geometryFactory.createPoint(new Coordinate(lng, lat));
    }

    public BookmarkResponse getBookmark(Long bookmarkId) {
        final Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
            .orElseThrow(() -> new BusinessException(BookmarkErrorCode.NOT_FOUND_BOOKMARK));
        return BookmarkMapper.INSTANCE.toDto(bookmark);
    }
}
