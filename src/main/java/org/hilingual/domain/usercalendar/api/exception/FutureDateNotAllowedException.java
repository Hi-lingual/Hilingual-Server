package org.hilingual.domain.usercalendar.api.exception;

import org.hilingual.common.exception.code.ErrorCode;
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
