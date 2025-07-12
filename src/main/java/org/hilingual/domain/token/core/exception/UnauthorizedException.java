package org.hilingual.domain.token.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.token.api.exception.JwtBaseException;
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