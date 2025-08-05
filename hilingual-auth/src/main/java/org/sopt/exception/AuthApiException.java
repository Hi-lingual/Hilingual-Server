package org.sopt.exception;


import org.sopt.exception.code.ErrorCode;

public abstract class AuthApiException extends AuthBaseException {
    protected AuthApiException(ErrorCode errorCode) { super(errorCode); }
}
