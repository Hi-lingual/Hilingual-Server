package org.sopt.feedalarm.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.feedalarm.exception.FeedAlarmCoreErrorCode;
import org.sopt.feedalarm.exception.InvalidTargetTypeException;

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
                .orElseThrow(() -> new InvalidTargetTypeException(FeedAlarmCoreErrorCode.INVALID_TARGET_TYPE));
    }
}
