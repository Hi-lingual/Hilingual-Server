package org.sopt.diary.exception;


import org.sopt.exception.code.ErrorCode;

public abstract class DiaryCoreException extends DiaryBaseException {
    protected DiaryCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
