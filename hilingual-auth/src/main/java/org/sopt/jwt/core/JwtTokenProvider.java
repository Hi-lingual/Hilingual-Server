package org.sopt.jwt.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.exception.*;
import org.sopt.type.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.support.AuthConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
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
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .clockSkewSeconds(60)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String type = claims.get("type", String.class);

            if ("ADMIN_STATIC".equals(type)) {
                log.info("만료된 ADMIN_STATIC 토큰을 허용합니다. (만료일: {})", e.getClaims().getExpiration());
                return claims;
            }
            throw e;

        } catch (JwtException e) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_TOKEN_ID);
        }
    }

    /** Authorization 헤더에서 Bearer 토큰 추출 */
    public String getJwtFromRequest(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        // 헤더가 없거나 Bearer로 시작하지 않으면 그냥 토큰 없음으로 간주
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(AuthConstants.BEARER_PREFIX)) {
            return null;
        }

        String token = bearerToken.substring(AuthConstants.BEARER_PREFIX.length());
        return StringUtils.hasText(token) ? token : null;
    }


    public String newSessionId() {
        return UUID.randomUUID().toString();
    }
}