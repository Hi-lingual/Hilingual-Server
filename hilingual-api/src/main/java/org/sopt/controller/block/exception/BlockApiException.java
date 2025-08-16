package org.sopt.controller.block.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class BlockApiException extends HilingualBaseException {

    protected BlockApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
