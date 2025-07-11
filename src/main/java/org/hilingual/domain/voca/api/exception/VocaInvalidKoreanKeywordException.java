package org.hilingual.domain.voca.api.exception;

import org.springframework.http.HttpStatus;

public class VocaInvalidKoreanKeywordException extends VocaApiException {

    public VocaInvalidKoreanKeywordException(VocaApiErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
