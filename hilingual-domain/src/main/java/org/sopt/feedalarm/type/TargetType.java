package org.sopt.feedalarm.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TargetType {
    DIARY(1),
    USER(2);

    private final int code;

    public static TargetType fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(t -> t.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TargetType code: " + code));
    }
}
