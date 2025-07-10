package org.hilingual.domain.security.token.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.security.token.api.exception.JwtBaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends JwtBaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}