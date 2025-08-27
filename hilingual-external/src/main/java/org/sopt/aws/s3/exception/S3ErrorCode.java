package org.sopt.aws.s3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    // 400
    UNSUPPORTED_CONTENT_TYPE(HttpStatus.BAD_REQUEST, 40027, "지원하지 않는 Content-Type 입니다."),
    INVALID_TMP_FILE_KEY(HttpStatus.BAD_REQUEST, 40028, "유효하지 않은 tmp fileKey 입니다."),
    INVALID_IMAGE_PURPOSE(HttpStatus.BAD_REQUEST, 40029, "image.purpose 값이 잘못되었습니다."),

    // 401
    AWS_CREDENTIALS_MISSING(HttpStatus.UNAUTHORIZED, 40104, "AWS 자격 증명이 없거나 유효하지 않습니다."),

    // 403
    S3_ACCESS_DENIED(HttpStatus.FORBIDDEN, 40302, "S3 접근이 거부되었습니다."),

    // 404
    S3_BUCKET_NOT_FOUND(HttpStatus.NOT_FOUND, 40405, "S3 버킷이 존재하지 않습니다."),

    // 500
    PRESIGN_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 50005, "Presigned URL 생성에 실패했습니다."),
    S3_COPY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 50006, "S3 객체 복사에 실패했습니다."),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 50007, "S3 원본 삭제에 실패했습니다."),
    CDN_DOMAIN_NOT_CONFIGURED(HttpStatus.INTERNAL_SERVER_ERROR, 50008, "CDN 도메인이 설정되어 있지 않습니다."),

    // 503
    S3_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, 50304, "S3 서비스가 일시적으로 사용 불가합니다."),
    ;

    public final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override public HttpStatus getHttpStatus(){ return httpStatus; }
    @Override public int getCode(){ return code; }
    @Override public String getMessage() { return message; }
}