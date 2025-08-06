package org.sopt.controller.diary.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class DiaryApiException extends HilingualBaseException {

    protected DiaryApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
