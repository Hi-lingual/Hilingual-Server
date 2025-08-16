package org.sopt.block.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnblockableUserException  extends BlockCoreException {
    public UnblockableUserException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.CONFLICT; }
}
