package org.sopt.alarmpreference.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.alarmpreference.exception.AlarmPreferenceCoreErrorCode;
import org.sopt.alarmpreference.exception.InvalidAlarmTypeException;
import org.sopt.exception.code.GlobalErrorCode;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AlarmType {
    FEED(1),
    MARKETING(2);

    private final int code;

    public static AlarmType from(String notiType) {
        if (notiType == null) {
            throw new InvalidAlarmTypeException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
        try {
            return AlarmType.valueOf(notiType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidAlarmTypeException(AlarmPreferenceCoreErrorCode.INVALID_ALARM_TYPE);
        }
    }

    public static AlarmType fromCode(int code) {
        return Arrays.stream(values())
                .filter(type -> type.code == code)
                .findFirst()
                .orElseThrow(() -> new InvalidAlarmTypeException(AlarmPreferenceCoreErrorCode.INVALID_ALARM_TYPE));
    }
}
