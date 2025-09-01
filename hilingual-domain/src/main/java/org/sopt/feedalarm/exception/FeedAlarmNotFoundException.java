package org.sopt.feedalarm.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class FeedAlarmNotFoundException extends FeedAlarmCoreException{
    public FeedAlarmNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}