package org.sopt.controller.userprofile.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserProfileApiErrorCode implements ErrorCode {
    IMAGE_PURPOSE_INVALID(HttpStatus.BAD_REQUEST, 40030, "image.purpose 값이 잘못되었습니다.");
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