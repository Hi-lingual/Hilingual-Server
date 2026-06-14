package org.sopt.controller.recoveryticket.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class RecoveryTicketApiException extends HilingualBaseException {
    protected RecoveryTicketApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}