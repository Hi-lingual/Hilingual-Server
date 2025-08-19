package org.sopt.controller.follow.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FollowApiErrorCode implements ErrorCode {
    SELF_FOLLOW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, 40025, "자기 자신은 팔로우할 수 없습니다."),
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

