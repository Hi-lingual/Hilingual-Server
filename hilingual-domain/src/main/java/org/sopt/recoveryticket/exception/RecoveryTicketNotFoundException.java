package org.sopt.recoveryticket.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class RecoveryTicketNotFoundException extends RecoveryTicketCoreException {
    public RecoveryTicketNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}