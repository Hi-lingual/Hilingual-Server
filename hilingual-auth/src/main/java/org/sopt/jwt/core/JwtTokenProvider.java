package org.sopt.jwt.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.exception.*;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.support.AuthConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {

    @Value("${jwt.secret-key}")
    private String secretKeyBase64;

    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh-token-expire-time}")
    @Getter
    private long REFRESH_TOKEN_EXPIRE_TIME;

    private SecretKey secretKey;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
        if (keyBytes.length < 64) {
            throw new InvalidSecretKeyException(AuthErrorCode.INVALID_SECRET_KEY);
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /** * AccessToken 생성
     * @param identifier 구버전 앱은 랜덤 SessionId, 신버전 앱은 DeviceUuid가 들어옴
     * */
    public String generateAccessToken(
            final long userId,
            final UserRole role,
            final AuthProvider provider,
            final String identifier
    ) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .subject(String.valueOf(userId))
                .claim(AuthConstants.USER_ID_CLAIM_NAME, userId)
                .claim(JwtClaimsKeys.ROLE, role.name())
                .claim(JwtClaimsKeys.PROVIDER, provider.name())
                .claim(JwtClaimsKeys.SESSION_ID, identifier)
                .claim(JwtClaimsKeys.TYPE, JwtClaimsKeys.ACCESS)
                .issuedAt(Date.from(now))
                .expiration(new Date(now.toEpochMilli() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    /** * RefreshToken 생성
     * @param identifier 구버전 앱은 랜덤 SessionId, 신버전 앱은 DeviceUuid가 들어옴
     * */
    public String generateRefreshToken(
            final long userId,
            final AuthProvider provider,
            final String identifier
    ) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .subject(String.valueOf(userId))
                .claim(AuthConstants.USER_ID_CLAIM_NAME, userId)
                .claim(JwtClaimsKeys.PROVIDER, provider.name())
                .claim(JwtClaimsKeys.SESSION_ID, identifier)
                .claim(JwtClaimsKeys.TYPE, JwtClaimsKeys.REFRESH)
                .issuedAt(Date.from(now))
                .expiration(new Date(now.toEpochMilli() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    /** 토큰 파싱 + 서명 검증 + 클레임 반환 (오차 60초 허용) */
    public Claims parseAndVerify(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Authorization 헤더에서 Bearer 토큰 추출 */
    public String getJwtFromRequest(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(bearerToken)) throw new TokenNotFoundException(AuthErrorCode.AUTH_HEADER_NOT_FOUND);
        if (!bearerToken.startsWith(AuthConstants.BEARER_PREFIX)) throw new InvalidAuthHeaderException(AuthErrorCode.INVALID_AUTH_HEADER);

        String token = bearerToken.substring(AuthConstants.BEARER_PREFIX.length());
        if (!StringUtils.hasText(token)) throw new TokenNotFoundException(AuthErrorCode.AUTH_TOKEN_NOT_FOUND);
        return token;
    }

    public String newSessionId() {
        return UUID.randomUUID().toString();
    }
}