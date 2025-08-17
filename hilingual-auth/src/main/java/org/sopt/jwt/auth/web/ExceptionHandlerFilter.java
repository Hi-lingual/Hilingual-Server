package org.sopt.jwt.auth.web;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.BusinessException;
import org.sopt.jwt.support.logsupport.ErrorLogger;
import org.sopt.jwt.support.logsupport.ErrorResponder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP Request 들어오면 1번째로 동작
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ErrorLogger errorLogger;
    private final ErrorResponder errorResponder;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            /** AuthenticationException 계열이면 throw 해서 EntryPoint 가 처리하도록 위임 */
            AuthErrorCode errorCode = mapExceptionToErrorCode(e);
            request.setAttribute("exception", errorCode);
            throw e;
        } catch (Exception e) {
            /** 그 외는 logging 해서 JOSN 으로 응답 */
            handleException(response, e);
        }
    }

    private void handleException(HttpServletResponse response, Exception e) {
       AuthErrorCode errorCode = mapExceptionToErrorCode(e);
        errorLogger.log(errorCode, e);
        errorResponder.write(response, errorCode);
    }

    private AuthErrorCode mapExceptionToErrorCode(Exception e) {
        if (e instanceof MalformedJwtException) return AuthErrorCode.MALFORMED_JWT_TOKEN;
        if (e instanceof IllegalArgumentException) return AuthErrorCode.TYPE_ERROR_JWT_TOKEN;
        if (e instanceof ExpiredJwtException) return AuthErrorCode.EXPIRED_JWT_TOKEN;
        if (e instanceof UnsupportedJwtException) return AuthErrorCode.UNSUPPORTED_JWT_TOKEN;
        if (e instanceof JwtException) return AuthErrorCode.UNKNOWN_JWT_TOKEN;
        if (e instanceof BusinessException){
            return (AuthErrorCode) ((BusinessException) e).getErrorCode();
        }
        return AuthErrorCode.INTERNAL_SERVER_ERROR;
    }
}