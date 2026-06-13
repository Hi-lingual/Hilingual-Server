package org.sopt.recoveryticket.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class RecoveryTicketCoreException extends HilingualBaseException {
    protected RecoveryTicketCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}