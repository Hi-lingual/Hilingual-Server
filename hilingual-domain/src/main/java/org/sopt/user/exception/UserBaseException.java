package org.sopt.user.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserBaseException extends HilingualBaseException {

    private final ErrorCode errorCode;
    public abstract HttpStatus getStatus();

}