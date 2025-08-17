package org.sopt.alarmpreference.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class AlarmPreferenceCoreException extends HilingualBaseException {
    protected AlarmPreferenceCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
