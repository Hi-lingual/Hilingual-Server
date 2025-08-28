package org.sopt.controller.recommend.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class RecommendForbiddenException extends RecommendApiException{
    public RecommendForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}