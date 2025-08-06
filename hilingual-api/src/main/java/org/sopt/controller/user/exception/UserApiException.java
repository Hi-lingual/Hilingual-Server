package org.sopt.controller.user.exception;

import org.sopt.diary.exception.DiaryBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class UserApiException extends DiaryBaseException {

    protected UserApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}