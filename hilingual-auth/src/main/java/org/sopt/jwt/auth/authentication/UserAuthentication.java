package org.sopt.jwt.auth.authentication;

import lombok.Getter;
import org.sopt.type.UserRole;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * 하이링구얼 서비스에 맞는 인증 객체(Authentication)를 만들기 위한
 * Spring Security 의 인증 객체 커스텀 클래스
 */
public class UserAuthentication extends AbstractAuthenticationToken {

    private final Long userId;
    @Getter
    private final UserRole role;

    private UserAuthentication(Long userId, UserRole role, boolean authenticated) {
        super(List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));
        this.userId = userId;
        this.role = role;
        super.setAuthenticated(authenticated);
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public static UserAuthentication create(Long userId, UserRole role) {
        return new UserAuthentication(userId, role, true);
    }
}