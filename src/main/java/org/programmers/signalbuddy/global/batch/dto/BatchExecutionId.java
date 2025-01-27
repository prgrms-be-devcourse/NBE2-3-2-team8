package org.programmers.signalbuddy.global.batch.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchExecutionId {

    private Long jobExecutionId;
    private Long stepExecutionId;
}
