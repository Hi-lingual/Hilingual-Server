package org.hilingual.auth.core.exception;

import org.hilingual.auth.api.exception.AuthBaseException;
import org.hilingual.common.exception.code.ErrorCode;

public abstract class AuthCoreException extends AuthBaseException {
    protected AuthCoreException(ErrorCode errorCode) { super(errorCode);}
}
