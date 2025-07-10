package org.hilingual.domain.user.api.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.diary.api.exception.DiaryBaseException;

public abstract class UserApiException extends DiaryBaseException {

    protected UserApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}