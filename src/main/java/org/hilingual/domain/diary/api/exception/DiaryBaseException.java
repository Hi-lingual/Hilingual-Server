package org.hilingual.domain.diary.api.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.code.ErrorCode;
import org.hilingual.common.exception.HilingualBaseException;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DiaryBaseException extends HilingualBaseException {

    private final ErrorCode errorCode;
    public abstract HttpStatus getStatus();

}