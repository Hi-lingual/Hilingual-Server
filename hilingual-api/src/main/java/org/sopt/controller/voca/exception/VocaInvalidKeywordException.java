package org.sopt.controller.voca.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class VocaInvalidKeywordException extends VocaApiException {

    public VocaInvalidKeywordException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
