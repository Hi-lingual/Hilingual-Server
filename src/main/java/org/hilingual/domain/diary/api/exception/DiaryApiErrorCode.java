package org.hilingual.domain.diary.api.exception;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DiaryApiErrorCode implements ErrorCode {
    DIARY_TOO_SHORT(HttpStatus.BAD_REQUEST, 40003, "10자 이상 작성해주세요."),
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