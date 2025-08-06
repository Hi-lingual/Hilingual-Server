package org.sopt.recommend.exception;


import org.sopt.exception.code.ErrorCode;

public abstract class RecommendCoreException extends RecommendBaseException {
    protected RecommendCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}