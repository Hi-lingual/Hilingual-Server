package org.hilingual.common.code;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
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
