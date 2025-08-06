package org.sopt.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class AuthCoreException extends HilingualBaseException {
    protected AuthCoreException(ErrorCode errorCode) { super(errorCode);}
}
