package org.sopt.controller.block.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class CannotSelfUnblockException extends BlockApiException {
    public CannotSelfUnblockException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.BAD_REQUEST; }
}
