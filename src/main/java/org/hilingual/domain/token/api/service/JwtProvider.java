package org.hilingual.domain.token.api.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.domain.token.api.dto.res.JwtTokenResponse;
import org.hilingual.domain.token.core.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    private final SecretKey secretKey;
    private final long ACCESS_EXPIRATION;
    private final long REFRESH_EXPIRATION;

    public JwtProvider(@Value("${jwt.secret-key}") String secret,
                       @Value("${jwt.access-expiration-seconds}") long accessExpiration,
                       @Value("${jwt.refresh-expiration-seconds}") long refreshExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ACCESS_EXPIRATION = accessExpiration;
        this.REFRESH_EXPIRATION = refreshExpiration;
    }

    public Claims parseTokenClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | SignatureException e) {
            log.error("[JWT 검증 실패] 서명 검증 실패 -> {}", e.getMessage());
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            log.error("[JWT 검증 실패] 손상된 JWT -> {}", e.getMessage());
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            log.error("[JWT 검증 실패] 만료된 JWT -> {}", e.getMessage());
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            log.error("[JWT 검증 실패] 지원되지 않는 JWT -> {}", e.getMessage());
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            log.error("[JWT 검증 실패] 실패 -> {}", e.getMessage());
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }
    }

    public void validateToken(String token) {
        try {
            parseTokenClaims(token);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }
    }

    public String generateAccessToken(Long userId) {
        log.info("[token] AccessToken 재발급: userId = {}", userId);
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "access")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ACCESS_EXPIRATION * 1000L))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        log.info("[token] RefreshToken 재발급: userId = {}", userId);
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + REFRESH_EXPIRATION * 1000L))
                .signWith(secretKey)
                .compact();
    }

    public JwtTokenResponse generateToken(Long userId) {
        log.info("[token] JwtProvider의 generateToken 진입");
        Date now = new Date();

        String accessToken = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "access")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ACCESS_EXPIRATION * 1000L))
                .signWith(secretKey)
                .compact();
        log.info("[token] JwtProvider의 generateToken에서 accessToken 생성 = {}", accessToken);

        String refreshToken = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + REFRESH_EXPIRATION * 1000L))
                .signWith(secretKey)
                .compact();
        log.info("[token] JwtProvider의 generateToken에서 refreshToken 생성 = {}", refreshToken);

        // TODO 일단 false로 넘기고 AuthService에서 처리
        return JwtTokenResponse.of(accessToken, refreshToken, false);
    }

    public Long getUserId(String token) {
        Claims claims = parseTokenClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    public Instant getExpiration(String token) {
        Claims claims = parseTokenClaims(token);
        return claims.getExpiration().toInstant();
    }

    public boolean isAccessToken(String token) {
        Claims claims = parseTokenClaims(token);
        return "access".equals(claims.get("type", String.class));
    }

    public boolean isRefreshToken(String token) {
        Claims claims = parseTokenClaims(token);
        return "refresh".equals(claims.get("type", String.class));
    }

    public long getRefreshExpirationMilliseconds() {
        return REFRESH_EXPIRATION * 1000L;
    }
}