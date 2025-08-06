package org.sopt.controller.user.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class UserApiException extends HilingualBaseException {

    protected UserApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}