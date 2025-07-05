package org.hilingual.common.dto;

import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.common.exception.code.SuccessCode;

public record BaseResponseDto<T>(
        int code,
        T data,
        String message
) {
    public static <T> BaseResponseDto<T> success(SuccessCode code, T data) {
        return new BaseResponseDto<>(code.getCode(), data, code.getMessage());
    }

    public static <T> BaseResponseDto<T> success(SuccessCode code) {
        return new BaseResponseDto<>(code.getCode(), null, code.getMessage());
    }

    public static <T> BaseResponseDto<T> fail(ErrorCode code) {
        return new BaseResponseDto<>(code.getCode(), null, code.getMessage());
    }
}