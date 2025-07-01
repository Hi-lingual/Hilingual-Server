package org.hilingual.domain.diary.api.exception;

import org.hilingual.common.code.ErrorCode;

public abstract class DiaryApiException extends DiaryBaseException{

    protected DiaryApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
