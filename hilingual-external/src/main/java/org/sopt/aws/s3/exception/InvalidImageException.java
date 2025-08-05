package org.sopt.aws.s3.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidImageException extends S3BaseException{
    public InvalidImageException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}