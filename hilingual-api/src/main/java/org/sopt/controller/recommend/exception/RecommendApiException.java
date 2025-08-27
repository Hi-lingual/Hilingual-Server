package org.sopt.controller.recommend.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class RecommendApiException extends HilingualBaseException {
    protected RecommendApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}