package org.hilingual.domain.usercalendar.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserCalendarTopicNotFoundException extends UserCalendarCoreException {

    public UserCalendarTopicNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
