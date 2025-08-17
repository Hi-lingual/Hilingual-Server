package org.sopt.jwt.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.AuthProvider;
import org.sopt.jwt.support.AuthConstant;
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
            throw new IllegalStateException("jwt.secret-key is too short for HS512 (need >= 64 bytes after Base64 decode)");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /** AccessToken 생성 */
    public String generateAccessToken(
            final long userId,
            final UserRole role,
            final AuthProvider provider,
            final String sessionId
    ) {
        final Instant now = Instant.now();
        return Jwts.builder()
                // typ 헤더는 없어도 무방하지만 유지하려면 다음 한 줄 사용
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .subject(String.valueOf(userId))                    // sub
                .claim(AuthConstant.USER_ID_CLAIM_NAME, userId)
                .claim("role", role.name())                      // 권한
                .claim("provider", provider.name())              // GOOGLE/APPLE
                .claim("sid", sessionId)                         // 세션 식별자
                .claim("type", "ACCESS_TOKEN")
                .claim("typ", "access")
                .issuedAt(Date.from(now))
                .expiration(new Date(now.toEpochMilli() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    /** RefreshToken 생성 */
    public String generateRefreshToken(
            final long userId,
            final AuthProvider provider,
            final String sessionId
    ) {
        final Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .subject(String.valueOf(userId))
                .claim(AuthConstant.USER_ID_CLAIM_NAME, userId)
                .claim("provider", provider.name())
                .claim("sid", sessionId)
                .claim("type", "REFRESH_TOKEN")
                .claim("typ", "refresh")
                .issuedAt(Date.from(now))
                .expiration(new Date(now.toEpochMilli() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    /** 토큰 파싱 + 서명 검증 + 클레임 반환 (오차 60초 허용) */
    public Claims getBody(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Authorization 헤더에서 Bearer 토큰 추출 */
    public String getJwtFromRequest(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthConstant.BEARER_PREFIX)) {
            return bearerToken.substring(AuthConstant.BEARER_PREFIX.length());
        }
        return null;
    }

    public String newSessionId() {
        return UUID.randomUUID().toString();
    }
}