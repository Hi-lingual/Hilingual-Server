package org.sopt.alarmpreference.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AlarmPreferenceCoreErrorCode implements ErrorCode {

    // 400
    INVALID_ALARM_TYPE(HttpStatus.BAD_REQUEST, 40017, "올바르지 않은 알림 타입입니다."),

    // 500
    NOT_FOUND_ALARM_PREFERENCE_ROW(HttpStatus.INTERNAL_SERVER_ERROR, 50009, "AlarmPreference row가 존재하지 않습니다.");
    ;

    public final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    @Override
    public int getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}