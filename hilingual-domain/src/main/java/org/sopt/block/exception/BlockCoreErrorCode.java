package org.sopt.block.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BlockCoreErrorCode implements ErrorCode {

    // 409
    UNBLOCKABLE_USER(HttpStatus.CONFLICT, 40901, "차단할 수 없는 사용자입니다.."),
    ALREADY_BLOCKED_USER(HttpStatus.CONFLICT, 40901, "이미 차단한 사용자입니다."),
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