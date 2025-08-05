package org.sopt.controller.voca.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class VocaInvalidSortTypeException extends VocaApiException {

    public VocaInvalidSortTypeException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

