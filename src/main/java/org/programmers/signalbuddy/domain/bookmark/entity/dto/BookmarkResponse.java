package org.programmers.signalbuddy.domain.bookmark.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@AllArgsConstructor
@Builder
public class BookmarkResponse {

    private Long bookmarkId;

    private Point coordinate;

    private String address;

    private Long memberId;

}
