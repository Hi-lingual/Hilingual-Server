package org.hilingual.external.s3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    // 400
    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, 40001, "허용되지 않은 이미지 확장자입니다."),
    INVALID_IMAGE_SIZE(HttpStatus.BAD_REQUEST, 40002, "이미지 용량은 최대 10MB까지 가능합니다."),

    // 500
    FILE_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "파일 처리 중 오류가 발생했습니다."),

    // 503
    S3_UPLOAD_FAILED(HttpStatus.SERVICE_UNAVAILABLE, 50300, "S3 이미지 업로드 중 오류가 발생했습니다."),
    S3_DELETE_FAILED(HttpStatus.SERVICE_UNAVAILABLE, 50301, "S3 이미지 삭제 중 오류가 발생했습니다.");
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