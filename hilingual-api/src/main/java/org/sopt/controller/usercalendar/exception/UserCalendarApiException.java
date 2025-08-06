package org.sopt.controller.usercalendar.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class UserCalendarApiException extends HilingualBaseException {
    protected UserCalendarApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
