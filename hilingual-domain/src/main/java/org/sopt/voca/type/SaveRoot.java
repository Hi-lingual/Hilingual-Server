package org.sopt.voca.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SaveRoot {
    MY(1),
    FEED(2),
    UNKNOWN(3); // 비공개, 삭제, 알 수 없는 경우

    private final int code;

    public static SaveRoot fromCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
