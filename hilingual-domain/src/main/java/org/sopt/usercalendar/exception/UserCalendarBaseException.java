package org.sopt.usercalendar.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;
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
