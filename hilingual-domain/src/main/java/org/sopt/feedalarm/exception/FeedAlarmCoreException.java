package org.sopt.feedalarm.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class FeedAlarmCoreException extends HilingualBaseException {
    protected FeedAlarmCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
