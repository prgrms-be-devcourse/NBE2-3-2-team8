package org.programmers.signalbuddy.domain.member.dto;

import lombok.Data;

@Data
public class MemberPagingResponse {

    private int page;
    private int size;
    private String sort;
}
