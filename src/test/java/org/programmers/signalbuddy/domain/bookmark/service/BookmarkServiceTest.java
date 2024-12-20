package org.programmers.signalbuddy.domain.bookmark.service;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkRequest;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.bookmark.repository.BookmarkRepository;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

class BookmarkServiceTest extends ServiceTest {

    private final GeometryFactory geometryFactory = new GeometryFactory();
    @Autowired
    private BookmarkService bookmarkService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;
    private Member member;
    private Bookmark bookmark;

    @BeforeEach
    void setup() {
        member = Member.builder().email("test@test.com").password("123456").role("USER")
            .nickname("tester").memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://test-image.com/test-123131").build();
        member = memberRepository.save(member);

        Point point = geometryFactory.createPoint(new Coordinate(126.553311, 36.66633));

        bookmark = Bookmark.builder().coordinate(point).address("Some Place").member(member)
            .build();
        bookmark = bookmarkRepository.save(bookmark);
    }

    @Test
    @DisplayName("즐겨찾기 등록 테스트")
    void createBookmark() {
        final BookmarkRequest request = BookmarkRequest.builder().lat(37.12345).lng(127.12345)
            .address("test").build();
        final BookmarkResponse response = bookmarkService.createBookmark(request, new User());
        final Optional<Bookmark> found = bookmarkRepository.findById(2L);

        final BookmarkResponse expected = BookmarkResponse.builder().bookmarkId(2L).lat(37.12345)
            .lng(127.12345).address("test").build();

        assertThat(response).isNotNull();
        assertThat(found).isPresent();
        assertThat(expected).isEqualTo(response);
        assertThat(found.get().getBookmarkId()).isEqualTo(expected.getBookmarkId());
        assertThat(found.get().getAddress()).isEqualTo(expected.getAddress());
        assertThat(found.get().getCoordinate().getX()).isEqualTo(expected.getLng());
        assertThat(found.get().getCoordinate().getY()).isEqualTo(expected.getLat());
    }

    @Test
    @DisplayName("즐겨찾기 수정 테스트")
    void updateBookmark() {
        final BookmarkRequest request = BookmarkRequest.builder().lat(37.12345).lng(127.12345)
            .address("test").build();

        final BookmarkResponse response = bookmarkService.updateBookmark(request, 1L, new User());
        final Optional<Bookmark> found = bookmarkRepository.findById(1L);

        final BookmarkResponse expected = BookmarkResponse.builder().bookmarkId(1L).lat(37.12345)
            .lng(127.12345).address("test").build();

        assertThat(response).isNotNull();
        assertThat(found).isPresent();
        assertThat(expected).isEqualTo(response);
        assertThat(found.get().getBookmarkId()).isEqualTo(1L);
        assertThat(found.get().getAddress()).isEqualTo(expected.getAddress());
        assertThat(found.get().getCoordinate().getX()).isEqualTo(expected.getLng());
        assertThat(found.get().getCoordinate().getY()).isEqualTo(expected.getLat());
    }

    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void deleteBookmark() {
        final Long bookmarkId = 1L;
        bookmarkService.deleteBookmark(bookmarkId, new User());
        final Optional<Bookmark> found = bookmarkRepository.findById(bookmarkId);

        assertThat(found.isPresent()).isFalse();
        assertThat(bookmarkRepository.existsById(bookmarkId)).isFalse();
    }
}