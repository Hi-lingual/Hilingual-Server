package org.sopt.noticedelivery.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class NoticeDeliveryCoreException extends HilingualBaseException {
    protected NoticeDeliveryCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
