package org.programmers.signalbuddy.domain.bookmark.service;

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
import org.programmers.signalbuddy.domain.member.entity.enums.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.dto.CustomUser2Member;
import org.programmers.signalbuddy.global.security.basic.CustomUserDetails;
import org.programmers.signalbuddy.global.support.ServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class BookmarkServiceTest extends ServiceTest {

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookmarkService bookmarkService;

    private Member member;
    private Bookmark bookmark;

    @BeforeEach
    void setup() {
        member = Member.builder().email("bookmark@bookmark.com").password("123456")
            .role(MemberRole.USER).nickname("bookmarkTest").memberStatus(MemberStatus.ACTIVITY)
            .profileImageUrl("https://book-test-image.com/test-123131").build();
        member = memberRepository.save(member);

        Point point = geometryFactory.createPoint(new Coordinate(126.553311, 36.66633));
        bookmark = Bookmark.builder().coordinate(point).address("Some Place").member(member)
            .build();
        bookmark = bookmarkRepository.save(bookmark);
    }


    @Test
    @DisplayName("즐겨찾기 등록 테스트")
    void createBookmark() {
        CustomUser2Member user = new CustomUser2Member(
            new CustomUserDetails(member.getMemberId(), "", "", "", "", MemberRole.USER,
                MemberStatus.ACTIVITY));

        final BookmarkRequest request = BookmarkRequest.builder().lat(37.12345).lng(127.12345)
            .address("test").build();
        final BookmarkResponse response = bookmarkService.createBookmark(request, user);
        final Optional<Bookmark> found = bookmarkRepository.findById(response.getBookmarkId());

        assertThat(response).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getAddress()).isEqualTo(response.getAddress());
        assertThat(found.get().getCoordinate().getX()).isEqualTo(response.getLng());
        assertThat(found.get().getCoordinate().getY()).isEqualTo(response.getLat());
    }

    @Test
    @DisplayName("즐겨찾기 수정 테스트")
    void updateBookmark() {
        CustomUser2Member user = new CustomUser2Member(
            new CustomUserDetails(member.getMemberId(), "", "", "", "", MemberRole.USER,
                MemberStatus.ACTIVITY));

        final BookmarkRequest request = BookmarkRequest.builder().lat(37.12345).lng(127.12345)
            .address("test").build();

        final BookmarkResponse response = bookmarkService.updateBookmark(request,
            bookmark.getBookmarkId(), user);
        final Optional<Bookmark> found = bookmarkRepository.findById(1L);

        assertThat(response).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getBookmarkId()).isEqualTo(1L);
        assertThat(found.get().getAddress()).isEqualTo(response.getAddress());
        assertThat(found.get().getCoordinate().getX()).isEqualTo(response.getLng());
        assertThat(found.get().getCoordinate().getY()).isEqualTo(response.getLat());
    }

    @Test
    @DisplayName("즐겨찾기 삭제 테스트")
    void deleteBookmark() {
        CustomUser2Member user = new CustomUser2Member(
            new CustomUserDetails(member.getMemberId(), "", "", "", "", MemberRole.USER,
                MemberStatus.ACTIVITY));

        bookmarkService.deleteBookmark(bookmark.getBookmarkId(), user);
        final Optional<Bookmark> found = bookmarkRepository.findById(bookmark.getBookmarkId());

        assertThat(found.isPresent()).isFalse();
        assertThat(bookmarkRepository.existsById(bookmark.getBookmarkId())).isFalse();
    }
}