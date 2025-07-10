package org.hilingual.domain.voca.api.exception;

import org.springframework.http.HttpStatus;

public class VocaInvalidSortTypeException extends VocaApiException {

    public VocaInvalidSortTypeException(VocaApiErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

