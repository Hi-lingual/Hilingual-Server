package org.sopt.usercalendar.exception;

import org.sopt.exception.code.ErrorCode;

public abstract class UserCalendarCoreException extends UserCalendarBaseException {
    protected UserCalendarCoreException(ErrorCode errorCode) {
        super(errorCode);
    }

}
