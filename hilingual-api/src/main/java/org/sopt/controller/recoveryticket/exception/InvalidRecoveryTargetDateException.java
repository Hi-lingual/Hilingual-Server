package org.sopt.controller.recoveryticket.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidRecoveryTargetDateException extends RecoveryTicketApiException {
    public InvalidRecoveryTargetDateException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}