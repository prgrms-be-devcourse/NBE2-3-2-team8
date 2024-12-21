package org.programmers.signalbuddy.domain.bookmark.entity.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.programmers.signalbuddy.domain.bookmark.entity.Bookmark;

@Mapper
public interface BookmarkMapper {
    BookmarkMapper INSTANCE = Mappers.getMapper(BookmarkMapper.class);


    @Mapping(source = "member.memberId", target = "memberId")
    BookmarkResponse toDto(Bookmark bookmark);

    List<AdminBookmarkResponse> toAdminDto(List<Bookmark> bookmarks);

}
