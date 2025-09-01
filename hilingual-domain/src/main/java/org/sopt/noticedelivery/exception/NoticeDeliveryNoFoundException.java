package org.sopt.noticedelivery.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class NoticeDeliveryNoFoundException extends NoticeDeliveryCoreException{
    public NoticeDeliveryNoFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}