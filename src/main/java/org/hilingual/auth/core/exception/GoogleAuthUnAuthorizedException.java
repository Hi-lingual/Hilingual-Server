package org.hilingual.auth.core.exception;

import org.hilingual.auth.api.exception.AuthBaseException;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class GoogleAuthUnAuthorizedException extends AuthBaseException {
    public GoogleAuthUnAuthorizedException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
