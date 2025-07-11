package org.hilingual.domain.usercalendar.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.base.HilingualBaseException;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class UserCalendarBaseException extends HilingualBaseException {
    private final ErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public abstract HttpStatus getStatus();
}
