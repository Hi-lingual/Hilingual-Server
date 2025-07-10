package org.hilingual.domain.voca.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.voca.api.exception.VocaBaseException;

public abstract class VocaCoreException extends VocaBaseException {
    protected VocaCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
