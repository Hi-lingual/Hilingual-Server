package org.sopt.notice.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NoticeCoreErrorCode implements ErrorCode {

    // 400
    INVALID_CATEGORY_TYPE(HttpStatus.BAD_REQUEST, 40020, "올바르지 않은 카테고리 타입입니다."),
    NOTICE_INACTIVE(HttpStatus.BAD_REQUEST, 40033, "비활성화된 공지입니다."),

    // 404
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, 40410,"id에 해당하는 공지가 존재하지 않습니다.")
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