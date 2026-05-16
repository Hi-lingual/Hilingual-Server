package org.sopt.userprofile.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidSameNicknameException extends UserProfileCoreException {

    public InvalidSameNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}