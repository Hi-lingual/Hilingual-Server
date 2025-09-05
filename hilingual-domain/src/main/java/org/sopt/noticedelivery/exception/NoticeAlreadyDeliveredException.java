package org.sopt.noticedelivery.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class NoticeAlreadyDeliveredException extends NoticeDeliveryCoreException{
    public NoticeAlreadyDeliveredException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}