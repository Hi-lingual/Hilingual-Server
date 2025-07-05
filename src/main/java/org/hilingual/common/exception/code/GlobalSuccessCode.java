package org.hilingual.common.exception.code;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalSuccessCode implements SuccessCode{
    OK(HttpStatus.OK, 20000, "성공했습니다."),
    OK_CUSTOM(HttpStatus.OK, 20100, "이건 default 아니지렁"),

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