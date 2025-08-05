package org.sopt.diary.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class AlreadyWrittenDiaryException extends DiaryCoreException  {
    public AlreadyWrittenDiaryException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}