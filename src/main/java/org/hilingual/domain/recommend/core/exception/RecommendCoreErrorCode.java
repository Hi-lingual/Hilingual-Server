package org.hilingual.domain.recommend.core.exception;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RecommendCoreErrorCode implements ErrorCode {

    // 404
    RECOMMEND_NOT_FOUND(HttpStatus.NOT_FOUND, 40403, "id 해당하는 추천표현이 없습니다."),
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