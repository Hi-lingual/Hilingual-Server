package org.hilingual.domain.userprofile.core.exception;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserProfileCoreErrorCode implements ErrorCode {

    USER_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, 40406, "회원 기본 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override public HttpStatus getHttpStatus() { return httpStatus; }
    @Override public int getCode() { return code; }
    @Override public String getMessage() { return message; }
}
