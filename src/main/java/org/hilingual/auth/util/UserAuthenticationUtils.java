package org.hilingual.auth.util;

import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.domain.token.core.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class UserAuthenticationUtils {

    private UserAuthenticationUtils() {
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }
        return (Long) authentication.getPrincipal();
    }
}
