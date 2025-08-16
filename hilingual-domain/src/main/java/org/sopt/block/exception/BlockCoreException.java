package org.sopt.block.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class BlockCoreException extends HilingualBaseException {
    protected BlockCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
