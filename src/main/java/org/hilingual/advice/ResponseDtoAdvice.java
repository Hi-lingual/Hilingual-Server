package org.hilingual.advice;

import lombok.NonNull;
import org.hilingual.common.exception.code.ErrorCode;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.common.exception.code.GlobalSuccessCode;
import org.hilingual.common.exception.code.SuccessCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(
        basePackages = "org.hilingual"
)
public class ResponseDtoAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !(returnType.getParameterType() == BaseResponseDto.class)
                && MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        if (body instanceof BaseResponseDto) {
            return body;
        }

        if (body instanceof ErrorCode errorCode) {
            return BaseResponseDto.fail(errorCode);
        }

        if (body instanceof SuccessCode successCode) {
            return BaseResponseDto.success(successCode);
        }
        return BaseResponseDto.success(GlobalSuccessCode.OK, body);
    }
}