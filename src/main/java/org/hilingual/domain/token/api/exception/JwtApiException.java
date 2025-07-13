package org.hilingual.domain.token.api.exception;

import org.hilingual.common.exception.code.ErrorCode;

public abstract class JwtApiException extends JwtBaseException {

    protected JwtApiException(ErrorCode errorCode) { super(errorCode); }
}
