package org.hilingual.common.code;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    // 400
    INVALID_ARGUMENTS(HttpStatus.BAD_REQUEST, 40000, "인자의 형식이 올바르지 않습니다."),

    // 401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40100, "인증되지 않은 사용자입니다."),

    // 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 40500, "지원하지 않는 HTTP 메서드입니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "서버 내부 오류입니다."),
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
