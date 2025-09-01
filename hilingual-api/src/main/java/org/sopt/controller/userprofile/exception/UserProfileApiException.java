package org.sopt.controller.userprofile.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class UserProfileApiException extends HilingualBaseException {
    protected UserProfileApiException(ErrorCode errorCode) { super(errorCode); }
}
