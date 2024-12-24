package org.programmers.signalbuddy.domain.bookmark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.member.MemberRole;
import org.programmers.signalbuddy.domain.member.entity.Member;
import org.programmers.signalbuddy.domain.member.entity.enums.MemberStatus;
import org.programmers.signalbuddy.domain.member.repository.MemberRepository;
import org.programmers.signalbuddy.global.support.RepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BookmarkRepositoryTest extends RepositoryTest {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private Member member;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        member = Member.builder().email("test@example.com").password("password123")
            .nickname("TestUser").profileImageUrl("http://example.com/profile.jpg").role(MemberRole.USER)
            .memberStatus(MemberStatus.ACTIVITY).build();
    }

    @DisplayName("즐겨찾기 목록 조회 테스트")
    @Test
    void testGetBookmarksWithPagination() {
        Member member1 = memberRepository.save(member);
        Point point = geometryFactory.createPoint(new Coordinate(127.12345, 37.12345));

        for (int i = 1; i <= 10; i++) {
            Bookmark bookmark = Bookmark.builder().address("Address " + i).coordinate(point)
                .member(member1).build();
            bookmarkRepository.save(bookmark);
        }

        /* H2 데이터베이스에서 ST_X 함수 이용 X*/
        Pageable pageable = PageRequest.of(0, 5);
        Page<BookmarkResponse> bookmarksPage = bookmarkRepository.findPagedByMember(pageable, 1L);

        assertThat(bookmarksPage).isNotNull();
        assertThat(bookmarksPage.getContent()).hasSize(5);
        assertThat(bookmarksPage.getTotalElements()).isEqualTo(10);
        assertThat(bookmarksPage.getTotalPages()).isEqualTo(2);
        assertThat(bookmarksPage.isFirst()).isTrue();
        assertThat(bookmarksPage.isLast()).isFalse();

        BookmarkResponse firstBookmark = bookmarksPage.getContent().get(0);
        assertThat(firstBookmark.getAddress()).isEqualTo("Address 1");

    }

}