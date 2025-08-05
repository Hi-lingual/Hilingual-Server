package org.sopt.userprofile.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserProfileAlreadyExistException extends UserProfileCoreException {

    public UserProfileAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
