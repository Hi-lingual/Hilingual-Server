package org.sopt.controller.follow.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class SelfFollowNotAllowedException extends FollowApiException{
    public SelfFollowNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}