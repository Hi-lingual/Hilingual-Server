package org.sopt.controller.auth.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class AppleServerErrorException extends AuthApiException {
    public AppleServerErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}