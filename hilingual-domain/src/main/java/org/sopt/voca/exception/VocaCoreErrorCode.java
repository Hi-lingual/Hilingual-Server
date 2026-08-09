package org.sopt.voca.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum VocaCoreErrorCode implements ErrorCode {

    // 400
    INVALID_SAVED_ROOT_TYPE(HttpStatus.BAD_REQUEST, 40421, "올바르지 않은 저장 경로입니다."),
    INVALID_MEMORIZATION_TARGET(HttpStatus.BAD_REQUEST, 40413, "요청에 본인 소유가 아니거나 존재하지 않는 단어가 포함되어 있습니다."),

    // 403
    VOCA_LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, 40304, "단어장은 최대 1000개까지만 저장할 수 있습니다."),

    // 404
    VOCA_NOT_FOUND(HttpStatus.NOT_FOUND, 40404, "phraseId에 해당하는 단어가 없습니다");


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
