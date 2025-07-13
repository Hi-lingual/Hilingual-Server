package org.hilingual.domain.recommend.core.exception;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.domain.recommend.api.exception.RecommendBaseException;

public abstract class RecommendCoreException extends RecommendBaseException {
    protected RecommendCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}