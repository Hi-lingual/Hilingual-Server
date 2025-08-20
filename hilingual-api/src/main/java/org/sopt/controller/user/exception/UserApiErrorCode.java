package org.sopt.controller.user.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserApiErrorCode implements ErrorCode {
    PROVIDER_LOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50304, "가입 경로 불러오기에 실패했습니다."),;

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