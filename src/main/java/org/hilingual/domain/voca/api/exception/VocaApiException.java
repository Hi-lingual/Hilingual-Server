package org.hilingual.domain.voca.api.exception;

import org.hilingual.common.exception.code.ErrorCode;

public abstract class VocaApiException extends VocaBaseException {
    protected VocaApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
