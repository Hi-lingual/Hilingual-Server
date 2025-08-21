package org.sopt.follow.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class FollowForbiddenByBlockException extends FollowCoreException{
    public FollowForbiddenByBlockException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}