package org.sopt.controller.device.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class DeviceApiException extends HilingualBaseException {
    protected DeviceApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}