package org.sopt.jwt.exception;

import org.sopt.exception.code.ErrorCode;

public abstract class JwtApiException extends JwtBaseException {

    protected JwtApiException(ErrorCode errorCode) { super(errorCode); }
}
