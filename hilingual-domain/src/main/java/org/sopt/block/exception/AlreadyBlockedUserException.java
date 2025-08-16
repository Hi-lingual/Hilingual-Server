package org.sopt.block.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyBlockedUserException extends BlockCoreException {
    public AlreadyBlockedUserException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.CONFLICT; }
}
