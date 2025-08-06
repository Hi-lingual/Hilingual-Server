package org.sopt.jwt.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.sopt.exception.code.GlobalErrorCode;
import org.sopt.jwt.constants.TokenConstants;
import org.sopt.jwt.domain.RefreshToken;
import org.sopt.jwt.dto.res.RefreshJwtTokenResponse;
import org.sopt.jwt.exception.UnauthorizedException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final StringRedisTemplate redisTemplate;
    private final JwtProvider jwtProvider;

    public String save(Long userId, String refreshToken) {
        long expiration = jwtProvider.getRefreshExpirationMilliseconds();
        String userKey = getUserRefreshTokensSetKey(userId);

        // JWT 문자열 키, userId 값, TTL 설정
        redisTemplate.opsForValue().set(refreshToken, String.valueOf(userId), expiration, TimeUnit.MILLISECONDS);
        // 해당하는 유저ID의 리프레시 토큰 Set에 현재 리프레시 토큰 추가
        redisTemplate.opsForSet().add(userKey, refreshToken);

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

            String userKeyList = getUserRefreshTokensSetKey(storedUserId);
            if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(userKeyList, refreshToken))) {
                log.warn("[Redis] String 키는 존재하나, 유저의 Set에 토큰 없음. 일관성 오류: userId={}, refreshToken={}", storedUserId, refreshToken);
                redisTemplate.delete(refreshToken);
                return Optional.empty();
            }

            log.info("[Redis] 리프레시 토큰 조회: Key='{}', Stored UserId='{}', Token Expiration={}",
                    refreshToken, storedUserId, Instant.ofEpochMilli(expirationTimestamp));

            return Optional.of(RefreshToken.builder()
                    .userId(storedUserId)
                    .refreshToken(refreshToken)
                    .build());
        } catch (UnauthorizedException e) {
            log.warn("[Redis] 리프레시 토큰 유효성 검증 실패 또는 만료: {}", refreshToken);
            // String 키 삭제
            redisTemplate.delete(refreshToken);
            // Set에서도 해당 토큰 제거
            // JWT로부터 userId를 다시 추출해서 Set에서 제거
            try {
                Long userIdFromToken = jwtProvider.getUserId(refreshToken);
                if (userIdFromToken != null) {
                    deleteFromUserSet(userIdFromToken, refreshToken);
                    log.info("[Redis] 유효성 검증 실패로 Set 멤버 제거: userId={}, refreshToken={}", userIdFromToken, refreshToken);
                }
            } catch (UnauthorizedException ex) {
                log.debug("[Redis] 유효성 검증 실패 처리 중 JWT userId 추출 실패: {}", refreshToken);
            }
            return Optional.empty();
        }
    }

    public void delete(String refreshToken) {
        // 키에서 userId 가져오기
        String tempUserId = redisTemplate.opsForValue().get(refreshToken);

        // refreshToken 삭제
        redisTemplate.delete(refreshToken);

        // 해당 유저의 refreshToken Set에서 현재 refreshToken 삭제
        if (tempUserId != null) {
            Long userId = Long.parseLong(tempUserId);
            deleteFromUserSet(userId, refreshToken);
            log.info("[Redis] 리프레쉬 토큰 삭제: Key={}, UserSetKey='{}'", refreshToken, getUserRefreshTokensSetKey(userId));
        } else {
            log.info("[Redis] 리프레쉬 토큰 삭제 (String 키에서 userId를 찾을 수 없음): Key={}", refreshToken);
            // userId를 String 키에서 얻지 못했지만, 혹시 Set에 남아있을 수 있으므로
            // JWT 자체에서 userId를 얻어 Set에서 제거 시도 (선택 사항)
            try {
                Long userIdFromToken = jwtProvider.getUserId(refreshToken);
                if (userIdFromToken != null) {
                    deleteFromUserSet(userIdFromToken, refreshToken);
                    log.info("[Redis] Redis String 키에 userId가 없었지만, JWT에서 추출하여 Set에서 토큰 제거: userId={}, refreshToken={}", userIdFromToken, refreshToken);
                }
            } catch (UnauthorizedException e) {
                // JWT 자체가 유효하지 않아 userId를 얻을 수 없는 경우 무시
                log.debug("[Redis] 삭제 처리 중 JWT userId 추출 실패: {}", refreshToken);
            }
        }
    }

    // 특정 유저의 모든 리프레시 토큰 삭제 (로그아웃 등)
    public void deleteAllRefreshTokensByUserId(Long userId) {
        String userSetKey = getUserRefreshTokensSetKey(userId);
        Set<String> refreshTokens = redisTemplate.opsForSet().members(userSetKey);

        if (refreshTokens != null && !refreshTokens.isEmpty()) {
            // 해당 유저의 모든 리프레시 토큰 String 키 삭제
            refreshTokens.forEach(token -> {
                redisTemplate.delete(token);
                log.info("[Redis] 유저({})의 개별 리프레시 토큰(String) 삭제: {}", userId, token);
            });
            // 유저의 Set 키 자체를 삭제
            redisTemplate.delete(userSetKey);
            log.info("[Redis] 유저({})의 모든 리프레시 토큰(Set) 삭제: {}", userId, userSetKey);
        } else {
            log.info("[Redis] 유저({})에 대한 리프레시 토큰이 Set에 없습니다.", userId);
        }
    }

    // 특정 유저의 Set에서 리프레시 토큰 멤버 삭제 (내부용)
    private void deleteFromUserSet(Long userId, String refreshToken) {
        String userSetKey = getUserRefreshTokensSetKey(userId);
        redisTemplate.opsForSet().remove(userSetKey, refreshToken);
    }

    // 유저의 Refresh Token Set 키 생성 유틸리티
    private String getUserRefreshTokensSetKey(Long userId) {
        return TokenConstants.USER_REFRESH_TOKENS_SET_KEY_PREFIX + userId + TokenConstants.REFRESH_TOKENS_SET_SUFFIX;
    }

    @Transactional
    public RefreshJwtTokenResponse reissue(String refreshToken) {
        Optional<RefreshToken> tokenOptional = find(refreshToken);
        if (tokenOptional.isEmpty()) {
            log.warn("[Reissue] Redis에서 유효한 리프레시 토큰을 찾을 수 없음: {}", refreshToken);
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        RefreshToken storedToken = tokenOptional.get();
        Long storedUserId = storedToken.getUserId();

        // Refresh Token 자체의 유효성 검증
        try {
            jwtProvider.validateToken(refreshToken);
        } catch (UnauthorizedException e) {
            log.warn("[Reissue] Refresh Token 자체 유효성 검증 실패 (JWT 만료 등): {}", refreshToken);
            delete(refreshToken);
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        // 토큰 타입 확인
        if (!jwtProvider.isRefreshToken(refreshToken)) {
            log.warn("[Reissue] 제공된 토큰은 리프레시 토큰 타입이 아님: {}", refreshToken);
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        // JWT에서 사용자 ID 추출
        Long userIdFromJwt = jwtProvider.getUserId(refreshToken);
        if (!storedUserId.equals(userIdFromJwt)) {
            log.error("[Reissue] Redis 저장된 userId와 JWT에서 추출된 userId 불일치. Redis userId: {}, JWT userId: {}", storedUserId, userIdFromJwt);
            // 불일치 발생 시 해당 토큰을 모두 삭제하고 에러 반환
            delete(refreshToken);
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        // 이전 토큰 삭제
        delete(refreshToken);
        log.info("[Reissue] 이전 리프레시 토큰 삭제 완료: {}", refreshToken);

        // 새로운 Access/Refresh Token 발급 및 저장
        String newAccessToken = jwtProvider.generateAccessToken(storedUserId);
        String newRefreshToken = jwtProvider.generateRefreshToken(storedUserId);

        save(storedUserId, newRefreshToken);
        log.info("[Reissue] 새로운 토큰 발급 및 저장 완료. userId: {}", storedUserId);

        return RefreshJwtTokenResponse.of(newAccessToken, newRefreshToken);
    }
}