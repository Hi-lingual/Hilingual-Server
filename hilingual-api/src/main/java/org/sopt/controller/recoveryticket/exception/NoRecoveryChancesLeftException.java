package org.sopt.controller.recoveryticket.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class NoRecoveryChancesLeftException extends RecoveryTicketApiException {
    public NoRecoveryChancesLeftException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}