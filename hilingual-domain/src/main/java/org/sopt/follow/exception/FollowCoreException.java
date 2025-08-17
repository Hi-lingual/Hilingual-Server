package org.sopt.follow.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class FollowCoreException extends HilingualBaseException {
    protected FollowCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
