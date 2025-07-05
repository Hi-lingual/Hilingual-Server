package org.hilingual.domain.diary.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.diary.api.exception.DiaryBaseException;

public abstract class DiaryCoreException extends DiaryBaseException {
    protected DiaryCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
