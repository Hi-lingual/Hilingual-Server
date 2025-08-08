package org.sopt.jwt.exception;


import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class JwtCoreException extends HilingualBaseException {
    protected JwtCoreException(ErrorCode errorCode) { super(errorCode); }
}
