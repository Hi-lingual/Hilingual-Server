package org.hilingual.domain.voca.api.exception;

import org.springframework.http.HttpStatus;

public class VocaInvalidSortTypeException extends VocaApiException {
    public VocaInvalidSortTypeException() {
        super(VocaApiErrorCode.INVALID_SORT_TYPE);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}

