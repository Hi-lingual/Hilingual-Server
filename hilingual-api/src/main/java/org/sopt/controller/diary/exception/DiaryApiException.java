package org.sopt.controller.diary.exception;

import org.sopt.diary.exception.DiaryBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class DiaryApiException extends DiaryBaseException {

    protected DiaryApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
