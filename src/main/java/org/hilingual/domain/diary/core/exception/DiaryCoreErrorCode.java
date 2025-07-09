package org.hilingual.domain.diary.core.exception;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DiaryCoreErrorCode implements ErrorCode {

    // 403
    DIARY_FORBIDDEN(HttpStatus.FORBIDDEN, 40301, "해당 유저의 일기가 아닙니다."),

    // 404
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, 40004, "id에 해당하는 일기가 존재하지 않습니다."),
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