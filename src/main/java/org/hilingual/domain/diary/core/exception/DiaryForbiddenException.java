package org.hilingual.domain.diary.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class DiaryForbiddenException extends DiaryCoreException{

    public DiaryForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}