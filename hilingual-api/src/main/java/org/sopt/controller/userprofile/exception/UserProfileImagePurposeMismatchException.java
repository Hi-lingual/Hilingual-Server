package org.sopt.controller.userprofile.exception;

import org.sopt.controller.user.exception.UserApiException;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserProfileImagePurposeMismatchException extends UserApiException {

    public UserProfileImagePurposeMismatchException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}