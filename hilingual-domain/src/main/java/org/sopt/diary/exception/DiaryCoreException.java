package org.sopt.diary.exception;


import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class DiaryCoreException extends HilingualBaseException {
    protected DiaryCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
