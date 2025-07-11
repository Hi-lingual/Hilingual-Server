package org.hilingual.advice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hilingual.auth.api.exception.AuthBaseException;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.domain.diary.api.exception.DiaryBaseException;
import org.hilingual.domain.token.api.exception.JwtBaseException;
import org.hilingual.external.openai.exception.OpenAiBaseException;
import org.hilingual.external.s3.exception.S3BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.hilingual.domain.voca.api.exception.VocaBaseException;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.error("MissingRequestHeaderException occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    @ExceptionHandler(AuthBaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleAuthBaseException(AuthBaseException e) {
        log.error("[AuthApiBaseException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(JwtBaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleJwtBaseException(JwtBaseException e) {
        log.error("[JwtBaseException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(DiaryBaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleDiaryBaseException(DiaryBaseException e) {
        log.error("[DiaryBaseException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(S3BaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleS3BaseException(S3BaseException e) {
        log.error("[S3BaseException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("[ConstraintViolationException] {}", e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    @ExceptionHandler(OpenAiBaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleOpenAiBaseException(OpenAiBaseException e) {
        log.error("[OpenAiBaseException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(VocaBaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleVocaBaseException(VocaBaseException e) {
        log.error("[VocaBaseException] message: {}", e.getMessage(), e);
        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto<Map<String, String>>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("[ValidationException] message: {}", e.getMessage(), e);

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<BaseResponseDto<Void>> handleNoPageFoundException(Exception e) {
        log.error("[NoHandlerFoundException] message: {}", e.getMessage(), e);

        ErrorCode errorCode = e instanceof HttpRequestMethodNotSupportedException
                ? GlobalErrorCode.METHOD_NOT_ALLOWED
                : GlobalErrorCode.NOT_FOUND_END_POINT;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(BaseResponseDto.fail(errorCode));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleMissingParam(MissingServletRequestParameterException e) {
        return ResponseEntity
                .status(GlobalErrorCode.NOT_FOUND_END_POINT.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.NOT_FOUND_END_POINT));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<Void>> handleException(Exception e) {
        log.error("[UnhandledException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INTERNAL_SERVER_ERROR));
    }



}
