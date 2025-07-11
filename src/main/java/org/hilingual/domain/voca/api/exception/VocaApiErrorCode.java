package org.hilingual.domain.voca.api.exception;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum VocaApiErrorCode implements ErrorCode {

    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, 40004, "올바르지 않은 정렬 방식입니다. sort는 1 또는 2여야 합니다."),
    INVALID_KEYWORD(HttpStatus.BAD_REQUEST, 40006, "검색어는 필수입니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
