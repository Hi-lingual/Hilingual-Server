package org.hilingual.domain.security.jwt;

import org.hilingual.common.exception.code.ErrorCode;

public abstract class JwtApiException extends JwtBaseException {

    protected JwtApiException(ErrorCode errorCode) { super(errorCode); }
}
