package org.sopt.exception.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public abstract class HilingualBaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public abstract HttpStatus getStatus();
}