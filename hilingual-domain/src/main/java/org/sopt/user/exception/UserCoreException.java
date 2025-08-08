package org.sopt.user.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class UserCoreException extends HilingualBaseException {
    protected UserCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
