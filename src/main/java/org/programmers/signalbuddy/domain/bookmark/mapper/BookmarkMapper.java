package org.programmers.signalbuddy.domain.bookmark.mapper;

import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkRequest;
import org.programmers.signalbuddy.domain.bookmark.dto.BookmarkResponse;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;
import org.programmers.signalbuddy.domain.member.entity.Member;

@Mapper
public interface BookmarkMapper {

    BookmarkMapper INSTANCE = Mappers.getMapper(BookmarkMapper.class);

    @Mapping(source = "point", target = "coordinate")
    Bookmark toEntity(BookmarkRequest bookmarkRequest, Point point, Member member);


    @Mapping(target = "lng", expression = "java(getLng(bookmark.getCoordinate()))")
    @Mapping(target = "lat", expression = "java(getLat(bookmark.getCoordinate()))")
    BookmarkResponse toDto(Bookmark bookmark);

    default double getLng(Point point) {
        return point.getX();
    }

    default double getLat(Point point) {
        return point.getY();
    }
}
