package org.hilingual.domain.voca.api.exception;

import org.springframework.http.HttpStatus;

public class VocaInvalidKeywordException extends VocaApiException {
    public VocaInvalidKeywordException(VocaApiErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
