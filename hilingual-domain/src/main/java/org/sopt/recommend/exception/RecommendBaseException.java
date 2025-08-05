package org.sopt.recommend.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RecommendBaseException extends HilingualBaseException {

    private final ErrorCode errorCode;
    public abstract HttpStatus getStatus();
}