package org.hilingual.domain.security.token.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.security.token.api.exception.JwtBaseException;

public abstract class JwtCoreException extends JwtBaseException {
    protected JwtCoreException(ErrorCode errorCode) { super(errorCode); }
}
