package org.sopt.feedalarm.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidTargetTypeException extends FeedAlarmCoreException {
    public InvalidTargetTypeException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }
}
