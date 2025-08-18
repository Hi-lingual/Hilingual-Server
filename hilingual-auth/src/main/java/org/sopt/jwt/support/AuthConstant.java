package org.sopt.jwt.support;

public class AuthConstant {
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
            "/api/v1/users/reissue"
    };

    private AuthConstant() {
    }
}