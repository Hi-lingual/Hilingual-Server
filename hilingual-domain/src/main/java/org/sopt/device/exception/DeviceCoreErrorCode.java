package org.sopt.device.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DeviceCoreErrorCode implements ErrorCode {

    // 400
    INVALID_TIMEZONE_FORMAT(HttpStatus.BAD_REQUEST, 40036, "유효하지 않은 타임존입니다."),

    // 404
    DEVICE_NOT_FOUND(HttpStatus.NOT_FOUND, 40412, "등록된 기기를 찾을 수 없습니다."),
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
