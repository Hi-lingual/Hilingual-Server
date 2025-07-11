package org.hilingual.domain.voca.core.exception;

import org.springframework.http.HttpStatus;

public class VocaSearchNotFoundException extends VocaCoreException {

    public VocaSearchNotFoundException(VocaCoreErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return getErrorCode().getHttpStatus();
    }

}
