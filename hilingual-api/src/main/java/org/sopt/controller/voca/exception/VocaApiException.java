package org.sopt.controller.voca.exception;

import org.sopt.exception.code.ErrorCode;
import org.sopt.recommend.exception.VocaBaseException;

public abstract class VocaApiException extends VocaBaseException {
    protected VocaApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
