package org.hilingual.auth.exception;

import org.hilingual.common.exception.code.ErrorCode;

public class GoogleAuthException extends AuthApiException {
    public GoogleAuthException(ErrorCode errorCode) { super(errorCode); }
}
