package org.sopt.controller.voca.exception;
import org.sopt.exception.code.ErrorCode;
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
