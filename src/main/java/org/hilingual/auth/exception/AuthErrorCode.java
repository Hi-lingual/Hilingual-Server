package org.hilingual.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    // 400
    INVALID_GOOGLE_ID_TOKEN(HttpStatus.BAD_REQUEST, 40007, "올바르지 않은 ID Token입니다."),
    INVALID_APPLE_TOKEN(HttpStatus.BAD_REQUEST, 40008, "올바르지 않은 Token입니다."),

    // 503
    AUTH_GOOGLE_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, 50304, "구글 로그인에서 에러가 발생했습니다."),
    AUTH_APPLE_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, 50305, "애플 로그인에서 에러가 발생했습니다.");

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
