package org.sopt.voca.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class VocaLimitExceededException extends VocaCoreException{
    public VocaLimitExceededException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
