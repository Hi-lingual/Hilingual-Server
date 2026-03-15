package org.sopt.controller.device.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class MissingDeviceIdentifierException extends DeviceApiException {
    public MissingDeviceIdentifierException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
