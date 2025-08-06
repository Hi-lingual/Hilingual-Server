package org.sopt.recommend.exception;


import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;

public abstract class RecommendCoreException extends HilingualBaseException {
    protected RecommendCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}