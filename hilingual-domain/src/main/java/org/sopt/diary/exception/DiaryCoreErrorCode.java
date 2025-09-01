package org.sopt.diary.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DiaryCoreErrorCode implements ErrorCode {

    // 403
    DIARY_FORBIDDEN(HttpStatus.FORBIDDEN, 40300, "공개되지 않은 일기입니다."),

    // 404
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, 40004, "id에 해당하는 일기가 존재하지 않습니다."),

    // 409
    ALREADY_WRITTEN_DIARY(HttpStatus.CONFLICT, 40901, "해당 날짜에 이미 작성된 일기가 있습니다."),
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