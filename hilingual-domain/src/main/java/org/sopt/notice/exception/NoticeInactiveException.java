package org.sopt.notice.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class NoticeInactiveException extends NoticeCoreException{
    public NoticeInactiveException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}