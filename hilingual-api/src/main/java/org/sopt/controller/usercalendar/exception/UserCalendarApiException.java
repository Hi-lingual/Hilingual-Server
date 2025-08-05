package org.sopt.controller.usercalendar.exception;

import org.sopt.exception.code.ErrorCode;
import org.sopt.usercalendar.exception.UserCalendarBaseException;

public abstract class UserCalendarApiException extends UserCalendarBaseException {
    protected UserCalendarApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
