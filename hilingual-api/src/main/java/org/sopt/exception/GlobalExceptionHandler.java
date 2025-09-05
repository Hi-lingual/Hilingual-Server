package org.sopt.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.sopt.controller.admin.exception.AdminApiErrorCode;
import org.sopt.exception.base.HilingualBaseException;
import org.sopt.dto.BaseResponseDto;
import org.sopt.exception.code.ErrorCode;
import org.sopt.exception.code.GlobalErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HilingualBaseException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleHilingualBaseException(HilingualBaseException e) {
        log.error("[HilingualBaseException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(e.getStatus())
                .body(BaseResponseDto.fail(e.getErrorCode()));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        log.error("MissingRequestHeaderException occurred: {}", e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("[ConstraintViolationException] {}", e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    //  JSON 요청 바디가 비어있거나, 잘못된 형식일 때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error("[HttpMessageNotReadableException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
    }

    // 지원하지 않는 Content-Type으로 요청할 때
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        log.error("[HttpMediaTypeNotSupportedException] message: {}", e.getMessage(), e);

        return ResponseEntity
                .status(GlobalErrorCode.INVALID_INPUT_VALUE.getHttpStatus())
                .body(BaseResponseDto.fail(GlobalErrorCode.INVALID_INPUT_VALUE));
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

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<BaseResponseDto<Void>> handleAuthorizationDenied(AuthorizationDeniedException e) {
        log.warn("[AuthorizationDeniedException] {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponseDto.fail(AdminApiErrorCode.FORBIDDEN_ADMIN));
    }

}
