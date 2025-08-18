package org.sopt.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class AuthBaseException extends HilingualBaseException {

    public AuthBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    public abstract HttpStatus getStatus();
}