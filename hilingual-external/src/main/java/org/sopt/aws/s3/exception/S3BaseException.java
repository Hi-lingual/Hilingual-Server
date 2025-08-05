package org.sopt.aws.s3.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.base.HilingualBaseException;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class S3BaseException extends HilingualBaseException {

    private final ErrorCode errorCode;

    public abstract HttpStatus getStatus();
}
