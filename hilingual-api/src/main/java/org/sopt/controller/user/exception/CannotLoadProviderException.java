package org.sopt.controller.user.exception;

import org.springframework.http.HttpStatus;
import org.sopt.exception.code.ErrorCode;

public class CannotLoadProviderException extends UserApiException {
    public CannotLoadProviderException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.INTERNAL_SERVER_ERROR; }
}
