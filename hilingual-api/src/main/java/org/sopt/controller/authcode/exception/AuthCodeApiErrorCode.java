package org.sopt.controller.authcode.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthCodeApiErrorCode implements ErrorCode {
    INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, 40099, "인증 코드가 일치하지 않습니다.");

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