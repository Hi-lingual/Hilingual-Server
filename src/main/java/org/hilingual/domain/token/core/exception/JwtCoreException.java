package org.hilingual.domain.token.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.token.api.exception.JwtBaseException;

public abstract class JwtCoreException extends JwtBaseException {
    protected JwtCoreException(ErrorCode errorCode) { super(errorCode); }
}
