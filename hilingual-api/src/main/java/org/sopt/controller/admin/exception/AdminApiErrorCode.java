package org.sopt.controller.admin.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AdminApiErrorCode implements ErrorCode {

    // 400
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, 40032, "유효하지 않은 공지 카테고리입니다."),

    // 403
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, 40305, "관리자 권한이 필요합니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() { return httpStatus; }

    @Override
    public int getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}