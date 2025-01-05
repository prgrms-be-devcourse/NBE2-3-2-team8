package org.programmers.signalbuddy.domain.crossroad.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SignalState {
    GREEN("protected-Movement-Allowed",true),   // 신호등이 녹색, 이동 보장 상태
    GRAY("permissive-Movement-Allowed",true), // 신호등이 황생, 이동 가능 상태
    RED("stop-And-Remain",false);               // 신호등이 적색, 정지 상태

    private String state;
    private boolean can_cross;

    @JsonValue // 데이터 직렬화 java -> JSON
    public String getState() {
        return name().toLowerCase();
    }

    @JsonCreator // 데이터 역직렬화 JSON -> java
    public static SignalState fromState(String state) {
        for(SignalState signalState : SignalState.values()) {
            if(signalState.state.equals(state)) {
                return signalState;
            }
        }
        throw new IllegalArgumentException("Unknown name: " + state);
    }
}
