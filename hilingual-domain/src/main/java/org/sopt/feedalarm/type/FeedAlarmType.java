package org.sopt.feedalarm.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FeedAlarmType {
    LIKE_DIARY(1),
    FOLLOW_USER(2);

    private final int code;

    public static FeedAlarmType fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(t -> t.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Type code: " + code));
    }
}