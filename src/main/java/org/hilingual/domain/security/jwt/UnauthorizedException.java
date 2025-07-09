package org.hilingual.domain.security.jwt;

import org.hilingual.common.exception.code.ErrorCode;
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