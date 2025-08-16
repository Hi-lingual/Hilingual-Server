package org.sopt.block.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class BlockedNotFoundException  extends BlockCoreException {
    public BlockedNotFoundException(ErrorCode errorCode) { super(errorCode); }

    @Override
    public HttpStatus getStatus() { return HttpStatus.NOT_FOUND; }
}
