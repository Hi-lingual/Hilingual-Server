package org.sopt.notice.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class NoticeCoreException extends HilingualBaseException {
    protected NoticeCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
