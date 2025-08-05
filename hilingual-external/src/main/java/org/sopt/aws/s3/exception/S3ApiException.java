package org.sopt.aws.s3.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class S3ApiException extends S3BaseException {
    public S3ApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }
}