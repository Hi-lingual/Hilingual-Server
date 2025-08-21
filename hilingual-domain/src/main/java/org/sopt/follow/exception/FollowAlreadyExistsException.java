package org.sopt.follow.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class FollowAlreadyExistsException extends FollowCoreException {
   public FollowAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}