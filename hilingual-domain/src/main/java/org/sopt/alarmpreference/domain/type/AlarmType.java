package org.sopt.alarmpreference.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AlarmType {
    FEED(1),
    MARKETING(2);

    private final int code;

    public static AlarmType fromCode(int code) {
        return Arrays.stream(values())
                .filter(type -> type.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid AlarmType code: " + code));
    }
}
