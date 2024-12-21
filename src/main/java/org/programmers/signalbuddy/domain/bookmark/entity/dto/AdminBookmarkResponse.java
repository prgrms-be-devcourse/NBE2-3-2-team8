package org.programmers.signalbuddy.domain.bookmark.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;


@Getter
@AllArgsConstructor
@Builder
public class AdminBookmarkResponse {
    private Long bookmarkId;

    private String address;

}
