package org.sopt.user.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidRegisterStatusException extends UserCoreException {
    public InvalidRegisterStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
