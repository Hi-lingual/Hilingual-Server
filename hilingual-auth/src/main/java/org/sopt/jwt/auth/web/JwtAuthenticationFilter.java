package org.sopt.jwt.auth.web;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.jwt.core.JwtClaimsKeys;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.auth.authentication.UserAuthenticationFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 2번째로 동작
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthenticationFactory userAuthenticationFactory;

    private static final AntPathMatcher PM = new AntPathMatcher();
    private static final List<String> SKIP = List.of(
            "/actuator/**",
            "/api/v1/users/reissue",
            "/test/jwt/token/issue"
    );

    private boolean shouldSkip(HttpServletRequest req) {
        String path = req.getServletPath();
        boolean matched = SKIP.stream().anyMatch(p -> PM.match(p, path));
        log.debug("skipCheck path='{}' matched={}", path, matched);
        return matched;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (shouldSkip(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        /** 헤더에서 토큰 추출 */
        final String token = jwtTokenProvider.getJwtFromRequest(request);
        /** 유효하면 파싱 및 검증해서 Claim 획득 */
        if (StringUtils.hasText(token)) {
            Claims claims = jwtTokenProvider.parseAndVerify(token);
            String type = claims.get(JwtClaimsKeys.TYPE, String.class);

            if (JwtClaimsKeys.ACCESS.equals(type)) {
                if (log.isDebugEnabled()) {
                    log.debug("JWT parsed. sub={}, sid={}", claims.getSubject(), claims.get("sid"));
                }
                userAuthenticationFactory.authenticateUser(claims, request);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Skip authentication. tokenType={}", type);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}