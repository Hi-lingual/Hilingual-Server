package org.sopt.diary.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class DiaryNotFoundException extends DiaryCoreException{
    public DiaryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}