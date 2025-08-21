package org.sopt.notice.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NoticeCoreErrorCode implements ErrorCode {

    // 400
    INVALID_CATEGORY_TYPE(HttpStatus.BAD_REQUEST, 40020, "올바르지 않은 카테고리 타입입니다.")
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