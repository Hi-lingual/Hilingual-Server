package org.hilingual.auth.api.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class AuthApiException extends AuthApiBaseException {
    protected AuthApiException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.SERVICE_UNAVAILABLE; }
}
