package org.hilingual.auth.api.exception;

import org.hilingual.common.exception.code.ErrorCode;

public abstract class AuthApiException extends AuthBaseException {
    protected AuthApiException(ErrorCode errorCode) { super(errorCode); }
}
