package org.sopt.controller.recoveryticket.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class RecoveryTicketAlreadyIssuedException extends RecoveryTicketApiException {
    public RecoveryTicketAlreadyIssuedException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}