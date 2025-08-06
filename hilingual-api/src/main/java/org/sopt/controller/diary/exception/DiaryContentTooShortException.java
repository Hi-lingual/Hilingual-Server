package org.sopt.controller.diary.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class DiaryContentTooShortException extends DiaryApiException {
    public DiaryContentTooShortException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
