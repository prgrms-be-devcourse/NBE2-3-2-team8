package org.programmers.signalbuddy.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class AdminBookmarkResponse {
    private Long bookmarkId;

    private String address;

}
