package org.hilingual.external.s3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.SuccessCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3SuccessCode implements SuccessCode {
    S3_UPLOAD_SUCCESS(HttpStatus.OK, 20000, "이미지 업로드 성공"),
    S3_DELETE_SUCCESS(HttpStatus.OK, 20000, "이미지 삭제 성공"),
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