package org.hilingual.advice;

import lombok.extern.slf4j.Slf4j;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.domain.diary.api.exception.DiaryBaseException;
import org.hilingual.external.s3.exception.S3BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiaryBaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleDiaryBaseException(DiaryBaseException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(S3BaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleS3BaseException(S3BaseException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto<Map<String, String>>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    // 존재하지 않는 요청에 대한 예외
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<BaseResponseDto<Void>> handleNoPageFoundException(Exception e) {
        ErrorCode errorCode = e instanceof HttpRequestMethodNotSupportedException
                ? GlobalErrorCode.METHOD_NOT_ALLOWED
                : GlobalErrorCode.NOT_FOUND_END_POINT;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponseDto.fail(errorCode));
    }

    // 기본 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INTERNAL_SERVER_ERROR));
    }
}