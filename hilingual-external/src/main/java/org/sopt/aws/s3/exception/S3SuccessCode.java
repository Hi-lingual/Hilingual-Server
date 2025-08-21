package org.sopt.aws.s3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3SuccessCode implements SuccessCode {
    S3_PRESIGN_ISSUE_SUCCESS(HttpStatus.OK, 20000, "prsigned-url 발급 성공"),
    ;

    public final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    @Override
    public int getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}