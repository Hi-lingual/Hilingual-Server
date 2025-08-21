package org.sopt.controller.follow.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class FollowApiException extends HilingualBaseException {

    protected FollowApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
