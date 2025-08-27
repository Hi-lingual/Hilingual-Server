package org.sopt.controller.userprofile.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.SuccessCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserProfileSuccessCode implements SuccessCode {
    NICKNAME_AVAILABLE(HttpStatus.OK, 20000, "사용 가능한 닉네임입니다."),
    NICKNAME_DUPLICATED(HttpStatus.OK, 20001, "중복된 닉네임입니다."),
    NICKNAME_SPECIAL_SYMBOLS(HttpStatus.OK, 20002, "특수문자, 이모지가 포함된 닉네임입니다."),
    NICKNAME_COUNT(HttpStatus.OK, 20003, "2자 미만이거나 10자 초과된 닉네임입니다."),
    NICKNAME_FORBIDDEN(HttpStatus.OK, 20004, "금지어가 포함된 닉네임입니다.");

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
