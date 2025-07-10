package org.hilingual.domain.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.domain.security.token.api.service.JwtProvider;
import org.hilingual.domain.security.constant.SecurityConstants;
import org.hilingual.domain.security.token.core.exception.UnauthorizedException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        final String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            jwtProvider.validateToken(token);
            Long userId = jwtProvider.getUserId(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        return (bearerToken != null && bearerToken.startsWith(SecurityConstants.BEARER_PREFIX)) ? bearerToken.substring(SecurityConstants.BEARER_PREFIX.length()) : null;
    }
}
