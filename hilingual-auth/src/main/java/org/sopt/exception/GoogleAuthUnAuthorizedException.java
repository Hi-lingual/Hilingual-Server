package org.sopt.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class GoogleAuthUnAuthorizedException extends AuthBaseException {
    public GoogleAuthUnAuthorizedException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
