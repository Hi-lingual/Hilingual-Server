package org.sopt.controller.auth.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class GoogleServerErrorException extends AuthApiException {
    public GoogleServerErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}