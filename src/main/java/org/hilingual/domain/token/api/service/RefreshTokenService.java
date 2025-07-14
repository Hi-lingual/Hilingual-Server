package org.hilingual.domain.token.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hilingual.auth.api.dto.res.RefreshJwtTokenResponse;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.domain.token.api.dto.res.JwtTokenResponse;
import org.hilingual.domain.token.core.domain.RefreshToken;
import org.hilingual.domain.token.core.exception.UnauthorizedException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;
    private final JwtProvider jwtProvider;

    public String save(Long userId, String refreshToken) {
        long expiration = jwtProvider.getRefreshExpirationMilliseconds();
        redisTemplate.opsForValue().set(refreshToken, String.valueOf(userId), expiration, TimeUnit.MILLISECONDS);

        log.info("[Redis] 리프레시 토큰 저장 : Key='{}', Value='{}', TTL={}ms, userId={}",
                refreshToken, userId, expiration, userId);

        return refreshToken;
    }

    public Optional<RefreshToken> find(String refreshToken) {
        String tempUserId = redisTemplate.opsForValue().get(refreshToken);

        if (tempUserId == null) {
            log.info("[Redis] 해당 리프레시 토큰 키 없거나 만료되어 삭제됨: {}", refreshToken);
            return Optional.empty();
        }

        Long storedUserId = Long.parseLong(tempUserId);

        try {
            jwtProvider.validateToken(refreshToken);
            long expirationTimestamp = jwtProvider.getExpiration(refreshToken).toEpochMilli();

            log.info("[Redis] 리프레시 토큰 조회: Key='{}', Stored UserId='{}', Token Expiration={}",
                    refreshToken, storedUserId, Instant.ofEpochMilli(expirationTimestamp));

            return Optional.of(RefreshToken.builder()
                    .userId(storedUserId)
                    .refreshToken(refreshToken)
                    .build());
        } catch (UnauthorizedException e) {
            log.warn("[Redis] 리프레시 토큰 유효성 검증 실패 또는 만료: {}", refreshToken);
            redisTemplate.delete(refreshToken);
            return Optional.empty();
        }
    }

    public void delete(String refreshToken) {
        redisTemplate.delete(refreshToken);
        log.info("[Redis] 리프레쉬 토큰 삭제: Key={}", refreshToken);
    }

    @Transactional
    public RefreshJwtTokenResponse reissue(String refreshToken) {
        jwtProvider.validateToken(refreshToken);

        if (!jwtProvider.isRefreshToken(refreshToken)) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        Optional<RefreshToken> tokenOptional = find(refreshToken);
        if (tokenOptional.isEmpty()) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }
        delete(refreshToken);

        String newAccessToken  = jwtProvider.generateAccessToken(userId);
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        save(userId, newRefreshToken);

        return RefreshJwtTokenResponse.of(newAccessToken, newRefreshToken);
    }
}