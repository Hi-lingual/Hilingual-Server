package org.hilingual.domain.usercalendar.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarBaseException;

public abstract class UserCalendarCoreException extends UserCalendarBaseException {
    protected UserCalendarCoreException(ErrorCode errorCode) {
        super(errorCode);
    }

}
