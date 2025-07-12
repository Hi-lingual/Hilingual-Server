package org.hilingual.auth.security.constant;

public class SecurityConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String[] AUTH_WHITE_LIST = {
        "/api/**", "/test/**", "/actuator/**", "/h2-console/**"
    };
}
