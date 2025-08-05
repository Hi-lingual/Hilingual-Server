package org.sopt.voca.exception;

import org.sopt.exception.code.ErrorCode;
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
