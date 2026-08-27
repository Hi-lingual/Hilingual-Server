package org.sopt.controller.widget.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class WidgetApiException extends HilingualBaseException {
    protected WidgetApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}