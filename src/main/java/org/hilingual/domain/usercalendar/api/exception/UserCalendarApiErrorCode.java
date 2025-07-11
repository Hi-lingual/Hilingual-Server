package org.hilingual.domain.usercalendar.api.exception;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserCalendarApiErrorCode implements ErrorCode {

    // 400
    FUTURE_DATE_NOT_ALLOWED(HttpStatus.BAD_REQUEST,40009, "미래 날짜에 대한 요청은 허용되지 않습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST,40010, "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식이어야 합니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
