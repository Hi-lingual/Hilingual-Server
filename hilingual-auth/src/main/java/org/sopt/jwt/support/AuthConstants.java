package org.sopt.jwt.support;

public class AuthConstants {
    public static final String USER_ID_CLAIM_NAME = "uid";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String CHARACTER_TYPE = "utf-8";
    public static final String CONTENT_TYPE = "application/json";

    // AuthConstant
    public static final String[] AUTH_WHITE_LIST = {
            "/actuator/**",
            "/callback",
            "/test/jwt/token/issue",
            "/api/v1/users/reissue",
            "/api/v1/auth/login",
            "/api/v1/users/reissue",
            "/api/v1/auth/verify",
            "/api/v1/users/profile/check",
            "/api/v1/admin/notices",
            "/api/v1/admin/notices/{noticeId}/delivery",
            "/api/v1/widget/topic"
    };

    private AuthConstants() {
    }
}