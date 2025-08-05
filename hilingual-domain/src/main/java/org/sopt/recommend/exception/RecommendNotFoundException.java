package org.sopt.recommend.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class RecommendNotFoundException extends RecommendCoreException{
    public RecommendNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}