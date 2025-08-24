package org.sopt.controller.diary.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class DiaryImagePurposeMismatchException extends DiaryApiException{
    public DiaryImagePurposeMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}