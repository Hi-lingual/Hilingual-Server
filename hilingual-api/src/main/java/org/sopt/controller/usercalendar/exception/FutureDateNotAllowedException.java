package org.sopt.controller.usercalendar.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class FutureDateNotAllowedException extends UserCalendarApiException {
    public FutureDateNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
