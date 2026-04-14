package org.sopt.firebase.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FCMErrorCode implements ErrorCode {

    FCM_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 50011, "FCM 푸시 발송에 실패했습니다."),
    FCM_INVALID_TOKEN(HttpStatus.BAD_REQUEST, 40041, "유효하지 않은 FCM 토큰입니다."),
    ;

    private final HttpStatus httpStatus;
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
