package org.sopt.jwt.support;

import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.UnAuthorizedException;

public class SecurityUtils {

    public static Object checkPrincipal(final Object principal) {
        if (AuthConstants.ANONYMOUS_USER.equals(principal)) {
            throw new UnAuthorizedException(AuthErrorCode.UNAUTHORIZED);
        }
        return principal;
    }
}