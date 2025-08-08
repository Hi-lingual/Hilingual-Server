package org.sopt.exception;


import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class AuthApiException extends HilingualBaseException {
    protected AuthApiException(ErrorCode errorCode) { super(errorCode); }
}
