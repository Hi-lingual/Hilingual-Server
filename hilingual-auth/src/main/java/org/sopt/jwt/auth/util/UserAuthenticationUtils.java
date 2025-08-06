package org.sopt.jwt.auth.util;

import org.sopt.exception.code.GlobalErrorCode;
import org.sopt.jwt.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserAuthenticationUtils {

    private UserAuthenticationUtils() {
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        if (authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        } else if (authentication.getPrincipal() instanceof String) {
            try {
                return Long.parseLong(authentication.getPrincipal().toString());
            } catch (NumberFormatException e) {
                throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
            }
        }

        throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
    }
}