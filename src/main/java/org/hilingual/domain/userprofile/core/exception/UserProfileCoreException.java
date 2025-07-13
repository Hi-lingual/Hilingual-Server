package org.hilingual.domain.userprofile.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.userprofile.api.exception.UserProfileBaseException;
import org.springframework.http.HttpStatus;

public abstract class UserProfileCoreException extends UserProfileBaseException {
    protected UserProfileCoreException(ErrorCode errorCode) {
        super(errorCode);
    }

    public abstract HttpStatus getStatus();
}
