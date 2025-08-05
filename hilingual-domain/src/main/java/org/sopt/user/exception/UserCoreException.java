package org.sopt.user.exception;

import org.sopt.exception.code.ErrorCode;

public abstract class UserCoreException extends UserBaseException {
    protected UserCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
