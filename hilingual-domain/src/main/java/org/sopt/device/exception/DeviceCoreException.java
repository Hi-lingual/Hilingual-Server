package org.sopt.device.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class DeviceCoreException extends HilingualBaseException {
    protected DeviceCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
