package org.sopt.jwt.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends JwtCoreException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}