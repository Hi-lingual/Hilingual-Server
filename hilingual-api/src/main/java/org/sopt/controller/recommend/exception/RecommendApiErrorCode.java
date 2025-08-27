package org.sopt.controller.recommend.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RecommendApiErrorCode implements ErrorCode {
    RECOMMEND_FORBIDDEN(HttpStatus.FORBIDDEN, 40300, "비공개 일기의 추천표현에는 접근 불가능합니다.")
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