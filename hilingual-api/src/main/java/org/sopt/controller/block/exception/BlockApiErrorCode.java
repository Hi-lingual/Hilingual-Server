package org.sopt.controller.block.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BlockApiErrorCode implements ErrorCode {
    // 400
    CANNOT_SELF_BLOCK(HttpStatus.BAD_REQUEST, 40015, "자기 자신은 차단할 수 없습니다."),
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
