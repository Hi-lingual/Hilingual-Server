package org.sopt.controller.widget.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum WidgetApiErrorCode implements ErrorCode {

    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, 40052, "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식이어야 합니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override public HttpStatus getHttpStatus() { return httpStatus; }
    @Override public int getCode() { return code; }
    @Override public String getMessage() { return message; }
}