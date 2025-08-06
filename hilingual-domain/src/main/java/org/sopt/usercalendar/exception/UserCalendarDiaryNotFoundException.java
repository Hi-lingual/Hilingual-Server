package org.sopt.usercalendar.exception;

import org.sopt.exception.code.ErrorCode;
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
