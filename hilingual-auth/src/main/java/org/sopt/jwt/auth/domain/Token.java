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

    private String deviceName;
    private DeviceType deviceType;
    private String osType;
    private String osVersion;
    private String appVersion;

    private Instant issuedAt;
    private Instant lastUsedAt;

    public static Token create(
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
}