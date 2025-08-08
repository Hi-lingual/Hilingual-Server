package org.sopt.jwt.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class JwtApiException extends HilingualBaseException {

    protected JwtApiException(ErrorCode errorCode) { super(errorCode); }
}
