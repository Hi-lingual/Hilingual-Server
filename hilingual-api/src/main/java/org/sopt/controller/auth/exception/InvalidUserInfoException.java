package org.sopt.controller.auth.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidUserInfoException extends AuthApiException {
    public InvalidUserInfoException(ErrorCode errorCode) {super(errorCode);}

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}