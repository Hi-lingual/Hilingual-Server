package org.sopt.alarmpreference.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidAlarmTypeException extends AlarmPreferenceCoreException {
    public InvalidAlarmTypeException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }
}
