package org.sopt.voca.exception;


import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class VocaCoreException extends HilingualBaseException {
    protected VocaCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
