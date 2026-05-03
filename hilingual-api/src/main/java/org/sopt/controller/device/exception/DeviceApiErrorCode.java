package org.sopt.controller.device.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DeviceApiErrorCode implements ErrorCode {

    //400
    MISSING_DEVICE_IDENTIFIER(HttpStatus.BAD_REQUEST, 40034, "기기 식별자가 누락되었습니다.(UUID 혹은 디바이스 정보를 확인해 주세요."),
    INVALID_DEVICE_TYPE(HttpStatus.BAD_REQUEST, 40035, "유효하지 않은 기기 타입입니다.")
    ;

    public final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
