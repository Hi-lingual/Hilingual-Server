package org.hilingual.domain.security.jwt;

import org.hilingual.common.exception.code.ErrorCode;

public abstract class JwtCoreException extends JwtBaseException {
    protected JwtCoreException(ErrorCode errorCode) { super(errorCode); }
}
