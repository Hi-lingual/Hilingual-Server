package org.hilingual.domain.usercalendar.api.exception;

import lombok.Getter;
import org.hilingual.common.exception.base.HilingualBaseException;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public abstract class UserCalendarBaseException extends HilingualBaseException {
    private final ErrorCode errorCode;

    protected UserCalendarBaseException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public abstract HttpStatus getStatus();
}
