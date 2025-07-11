package org.hilingual.domain.voca.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.voca.api.exception.VocaApiErrorCode;
import org.hilingual.domain.voca.core.exception.VocaCoreErrorCode;
import org.springframework.http.HttpStatus;


public class VocaNotFoundException extends VocaCoreException {

    public VocaNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
