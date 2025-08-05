package org.sopt.userprofile.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class UserProfileCoreException extends UserProfileBaseException {
    protected UserProfileCoreException(ErrorCode errorCode) {
        super(errorCode);
    }

    public abstract HttpStatus getStatus();
}
