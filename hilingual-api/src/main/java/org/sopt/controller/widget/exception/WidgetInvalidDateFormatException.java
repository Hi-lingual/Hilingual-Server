package org.sopt.controller.widget.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class WidgetInvalidDateFormatException extends WidgetApiException {

    public WidgetInvalidDateFormatException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}