package org.sopt.controller.user.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotLoadProviderException extends UserApiException {
    public CannotLoadProviderException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.INTERNAL_SERVER_ERROR; }
}
