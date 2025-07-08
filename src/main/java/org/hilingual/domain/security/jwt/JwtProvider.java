package org.hilingual.domain.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long ACCESS_EXPIRATION_MILLISECONDS;
    private final long REFRESH_EXPIRATION_MILLISECONDS;

    public JwtProvider(@Value("${jwt.secret-key}") String secret,
                       @Value("${jwt.access-expiration-seconds}") long accessTokenExpirePeriod,
                       @Value("${jwt.refresh-expiration-seconds}") long refreshTokenExpirePeriod) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ACCESS_EXPIRATION_MILLISECONDS = accessTokenExpirePeriod * 1000;
        this.REFRESH_EXPIRATION_MILLISECONDS = refreshTokenExpirePeriod * 1000;
    }

    public JwtToken generateToken(Long userId) {
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_EXPIRATION_MILLISECONDS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_EXPIRATION_MILLISECONDS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return new JwtToken(accessToken, refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }
}