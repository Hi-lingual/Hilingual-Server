package org.sopt.follow.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FollowCoreErrorCode implements ErrorCode {

    // 403
    FOLLOW_FORBIDDEN_BY_BLOCK(HttpStatus.FORBIDDEN, 40301, "차단 관계로 인해 팔로우할 수 없습니다."),

    // 409
    FOLLOW_ALREADY_EXISTS(HttpStatus.CONFLICT, 40904, "이미 팔로우한 유저입니다."),
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
