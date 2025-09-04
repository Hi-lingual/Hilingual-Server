package org.sopt.alarmpreference.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundAlarmPreferenceRow extends AlarmPreferenceCoreException{
    public NotFoundAlarmPreferenceRow(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}