package org.sopt.exception;

import org.sopt.exception.code.ErrorCode;

public abstract class AuthCoreException extends AuthBaseException {
    protected AuthCoreException(ErrorCode errorCode) { super(errorCode);}
}
