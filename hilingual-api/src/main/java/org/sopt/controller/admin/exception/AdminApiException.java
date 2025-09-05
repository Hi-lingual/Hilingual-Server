package org.sopt.controller.admin.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class AdminApiException extends HilingualBaseException {
    public AdminApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}