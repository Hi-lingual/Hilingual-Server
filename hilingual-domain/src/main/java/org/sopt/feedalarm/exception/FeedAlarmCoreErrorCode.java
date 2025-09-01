package org.sopt.feedalarm.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FeedAlarmCoreErrorCode implements ErrorCode {

    // 400
    INVALID_FEED_ALARM_TYPE(HttpStatus.BAD_REQUEST, 40018, "올바르지 않은 피드 알림 타입입니다."),
    INVALID_TARGET_TYPE(HttpStatus.BAD_REQUEST, 40019, "올바르지 않은 타켓 타입입니다."),

    // 404
    FEED_ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, 40409, "id에 해당하는 알람이 없습니다.")

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