package org.sopt.usercalendar.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class UserCalendarCoreException extends HilingualBaseException {
    protected UserCalendarCoreException(ErrorCode errorCode) {
        super(errorCode);
    }

}
