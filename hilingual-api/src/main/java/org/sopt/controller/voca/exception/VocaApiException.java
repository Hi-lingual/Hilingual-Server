package org.sopt.controller.voca.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class VocaApiException extends HilingualBaseException {
    protected VocaApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
