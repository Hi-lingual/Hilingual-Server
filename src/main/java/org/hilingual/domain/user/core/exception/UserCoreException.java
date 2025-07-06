package org.hilingual.domain.user.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.user.api.exception.UserBaseException;

public abstract class UserCoreException extends UserBaseException {
    protected UserCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
