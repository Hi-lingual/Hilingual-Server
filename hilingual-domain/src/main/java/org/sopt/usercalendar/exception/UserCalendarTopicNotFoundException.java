package org.sopt.usercalendar.exception;

import org.sopt.exception.code.ErrorCode;
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
