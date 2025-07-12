package org.hilingual.domain.userprofile.core.exception;

import org.hilingual.common.exception.base.HilingualBaseException;
import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarBaseException;
import org.springframework.http.HttpStatus;

public abstract class UserProfileCoreException extends UserCalendarBaseException {
    protected UserProfileCoreException(ErrorCode errorCode) {
        super(errorCode);
    }

    public abstract HttpStatus getStatus();
}
