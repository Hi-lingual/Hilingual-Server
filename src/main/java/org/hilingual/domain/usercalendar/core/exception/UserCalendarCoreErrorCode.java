package org.hilingual.domain.usercalendar.core.exception;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserCalendarCoreErrorCode implements ErrorCode{

    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, 40405, "해당 날짜에 작성된 일기가 없습니다."),
    TOPIC_NOT_FOUND(HttpStatus.NOT_FOUND, 40406, "해당 날짜에 주제가 존재하지 않습니다.");


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
