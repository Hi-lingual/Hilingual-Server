package org.sopt.controller.feed.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class FeedApiException extends HilingualBaseException {

    protected FeedApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}