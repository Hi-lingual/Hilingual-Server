package org.sopt.jwt.auth.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.auth.domain.type.DeviceType;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "token", timeToLive = 60 * 60 * 24 * 14)
public class Token {

    @Id
    private String id;

    @Indexed
    private Long userId;

    @Indexed
    private AuthProvider authProvider;

    @Indexed
    private String refreshTokenHash;

    private String deviceUuid;

    /* TODO [Soft Migration]
        구버전 앱 호환성을 위해 당분간 유지하며, 신버전 앱에서는 null로 들어옴
        기존 앱 Version 사용 유저가 없는 경우 완전 삭제 요망
     */
    private String deviceName;
    private DeviceType deviceType;
    private String osType;
    private String osVersion;
    private String appVersion;

    private Instant issuedAt;
    private Instant lastUsedAt;

    // 구버전
    public static Token createLegacy(
            Long userId,
            AuthProvider authProvider,
            String refreshTokenHash,
            String deviceName,
            DeviceType deviceType,
            String osType,
            String osVersion,
            String appVersion
    ){
        return Token.builder()
                .id(userId + ":" + UUID.randomUUID()) // userId:sessionId
                .userId(userId)
                .authProvider(authProvider)
                .refreshTokenHash(refreshTokenHash)
                .deviceName(deviceName)
                .deviceType(deviceType)
                .osType(osType)
                .osVersion(osVersion)
                .appVersion(appVersion)
                .issuedAt(Instant.now())
                .lastUsedAt(Instant.now())
                .build();
    }

    // 신버전
    public static Token create(
            Long userId,
            AuthProvider authProvider,
            String refreshTokenHash,
            String deviceUuid
    ){
        return Token.builder()
                .id(userId + ":" + deviceUuid)
                .userId(userId)
                .authProvider(authProvider)
                .refreshTokenHash(refreshTokenHash)
                .deviceUuid(deviceUuid)
                .issuedAt(Instant.now())
                .lastUsedAt(Instant.now())
                .build();
    }
}