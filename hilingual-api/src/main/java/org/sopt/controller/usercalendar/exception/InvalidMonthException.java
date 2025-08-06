package org.sopt.controller.usercalendar.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidMonthException extends UserCalendarApiException {
    public InvalidMonthException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
