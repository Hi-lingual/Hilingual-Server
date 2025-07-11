package org.hilingual.domain.usercalendar.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarBaseException;
import org.springframework.http.HttpStatus;

public class UserCalendarDiaryNotFoundException extends UserCalendarBaseException {
    public UserCalendarDiaryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
