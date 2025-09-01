package org.sopt.controller.authcode.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class AuthCodeApiException extends HilingualBaseException {

    protected AuthCodeApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}