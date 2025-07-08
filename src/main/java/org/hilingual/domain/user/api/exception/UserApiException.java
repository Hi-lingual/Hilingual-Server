package org.hilingual.domain.user.api.exception;

import org.hilingual.common.exception.code.ErrorCode;

public abstract class UserApiException extends UserBaseException {
    protected UserApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}