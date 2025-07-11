package org.hilingual.domain.voca.api.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class VocaInvalidKoreanKeywordException extends VocaApiException {

    public VocaInvalidKoreanKeywordException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
