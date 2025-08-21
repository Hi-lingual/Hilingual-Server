package org.sopt.jwt.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sopt.dto.BaseResponseDto;
import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.code.ErrorCode;
import org.sopt.jwt.support.AuthConstants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 401 전용 응답(AuthenticationException)
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        if (errorCode == null) {
            errorCode = AuthErrorCode.WRONG_ENTRY_POINT;
        }

        response.setContentType(AuthConstants.CONTENT_TYPE);
        response.setCharacterEncoding(AuthConstants.CHARACTER_TYPE);
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(
                objectMapper.writeValueAsString(BaseResponseDto.fail(errorCode))
        );
    }
}