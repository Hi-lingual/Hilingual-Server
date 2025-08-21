package org.sopt.exception;

import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public abstract class AuthBaseException extends HilingualBaseException {

    protected AuthBaseException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public abstract HttpStatus getStatus(); // 상태 코드는 하위 클래스에서 결정
}