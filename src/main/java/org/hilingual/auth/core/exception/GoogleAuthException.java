package org.hilingual.auth.core.exception;

import org.hilingual.auth.api.exception.AuthApiException;
import org.hilingual.common.exception.code.ErrorCode;

public class GoogleAuthException extends AuthApiException {
    public GoogleAuthException(ErrorCode errorCode) { super(errorCode); }
}
