package org.programmers.signalbuddy.domain.crossroad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CrossroadStateApiResponse {

    @JsonProperty("itstId")
    private String crossroadApiId;

    // e,w,s,n,ne,nw,se,sw 8방위
    // p : 사람, rs: remain second (남은 시간), sn: state name (상태 이름)

    @JsonProperty("ntPdsgRmdrCs")
    private int nprs;

    @JsonProperty("etPdsgRmdrCs")
    private int eprs;

    @JsonProperty("stPdsgRmdrCs")
    private int sprs;

    @JsonProperty("wtPdsgRmdrCs")
    private int wprs;

    @JsonProperty("nePdsgRmdrCs")
    private int neprs;

    @JsonProperty("nwPdsgRmdrCs")
    private int nwprs;

    @JsonProperty("swPdsgRmdrCs")
    private int swprs;

    @JsonProperty("sePdsgRmdrCs")
    private int seprs;

    @JsonProperty("ntPdsgStatNm")
    private SignalState npsn;

    @JsonProperty("etPdsgStatNm")
    private SignalState epsn;

    @JsonProperty("wtPdsgStatNm")
    private SignalState wpsn;

    @JsonProperty("stPdsgStatNm")
    private SignalState spsn;

    @JsonProperty("nePdsgStatNm")
    private SignalState nepsn;

    @JsonProperty("nwPdsgStatNm")
    private SignalState nwpsn;

    @JsonProperty("sePdsgStatNm")
    private SignalState sepsn;

    @JsonProperty("swPdsgStatNm")
    private SignalState swpsn;

}
