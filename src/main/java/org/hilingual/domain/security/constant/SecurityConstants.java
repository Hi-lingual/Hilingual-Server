package org.hilingual.domain.security.constant;

public class SecurityConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String[] AUTH_WHITE_LIST = {
        "api/v1/auth/login", "api/v1/users/profile?nickname", "/api/v1/auth/reissue", "test/jwt/**"
    };
}
