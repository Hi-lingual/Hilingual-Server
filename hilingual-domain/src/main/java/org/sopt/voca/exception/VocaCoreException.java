package org.sopt.voca.exception;


import org.sopt.exception.code.ErrorCode;
import org.sopt.recommend.exception.VocaBaseException;

public abstract class VocaCoreException extends VocaBaseException {
    protected VocaCoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
