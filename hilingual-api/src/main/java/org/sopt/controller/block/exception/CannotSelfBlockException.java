package org.sopt.controller.block.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotSelfBlockException extends BlockApiException {
    public CannotSelfBlockException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }
}
