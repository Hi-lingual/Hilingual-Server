package org.sopt.noticedelivery.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class NoticeNotFoundException extends NoticeDeliveryCoreException{
    public NoticeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}