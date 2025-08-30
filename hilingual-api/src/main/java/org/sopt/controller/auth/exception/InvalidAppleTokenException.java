package org.sopt.controller.auth.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidAppleTokenException extends AuthApiException {
    public InvalidAppleTokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}