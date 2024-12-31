package org.programmers.signalbuddy.domain.bookmark.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AdminBookmarkResponse {

    private Long bookmarkId;

    private String address;

}
