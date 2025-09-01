package org.sopt.noticedelivery.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NoticeDeliveryErrorCode implements ErrorCode {
    NOTICE_DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, 40499, "해당 공지는 존재하지 않거나 이 유저에게 발송되지 않았습니다."),
    ;

    public final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public org.springframework.http.HttpStatus getHttpStatus() {
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