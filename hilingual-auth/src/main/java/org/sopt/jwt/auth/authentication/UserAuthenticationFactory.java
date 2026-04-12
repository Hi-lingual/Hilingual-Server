package org.sopt.jwt.auth.authentication;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.sopt.jwt.support.AuthConstants;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * 1. Claims 에서 userId, role 읽어서 Authentication 객체 생성
 * 2. SecurityContextHolder 에 저장
 */
@Component
public class UserAuthenticationFactory {

    public void authenticateUser(Claims claims, HttpServletRequest request) {
        // AccessToken 만 통과
        String type = claims.get("type", String.class);
        if (!"ACCESS_TOKEN".equals(type) && !"ADMIN_STATIC".equals(type)) {
            throw new BadCredentialsException("Access token required");
        }

        // userId: 클레임 → sub 순으로 파싱
        Long userId = null;
        Object idObj = claims.get(AuthConstants.USER_ID_CLAIM_NAME);
        if (idObj instanceof Number n) userId = n.longValue();
        if (userId == null) userId = Long.valueOf(claims.getSubject());

        // role
        UserRole role = UserRole.USER;
        String roleStr = claims.get("role", String.class);
        if (roleStr != null) {
            try { role = UserRole.valueOf(roleStr); } catch (IllegalArgumentException ignore) {}
        }

        UserAuthentication auth = UserAuthentication.create(userId, role);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}