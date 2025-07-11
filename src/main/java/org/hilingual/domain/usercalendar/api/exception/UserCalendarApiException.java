package org.hilingual.domain.usercalendar.api.exception;

import org.hilingual.common.exception.code.ErrorCode;

public abstract class UserCalendarApiException extends UserCalendarBaseException {
    protected UserCalendarApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
