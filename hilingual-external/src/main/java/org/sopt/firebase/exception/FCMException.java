package org.sopt.firebase.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class FCMException extends HilingualBaseException {

    public FCMException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return getErrorCode().getHttpStatus();
    }

    public boolean isInvalidToken() {
        return getErrorCode() == FCMErrorCode.FCM_INVALID_TOKEN;
    }
}
