package org.sopt.controller.token;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.auth.dto.SocialLoginReq;
import org.sopt.controller.auth.dto.SocialLoginRes;
import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.InvalidTokenException;
import org.sopt.exception.TokenNotFoundException;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.auth.domain.Token;
import org.sopt.jwt.auth.domain.TokenRepository;
import org.sopt.jwt.auth.dto.ReissueTokensRes;
import org.sopt.jwt.core.JwtClaimsKeys;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.core.TokenHasher;
import org.sopt.jwt.core.TokenId;
import org.sopt.jwt.support.AuthConstants;
import org.sopt.user.facade.UserFacade;
import org.sopt.user.type.RegisterStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final TokenHasher tokenHasher;
    private final UserFacade userFacade;

    @Transactional
    public ReissueTokensRes reissue(String refreshToken) {

        final Claims claims = jwtTokenProvider.parseAndVerify(refreshToken);

        // RefreshToken 이 맞는지 검증
        final String type = claims.get(JwtClaimsKeys.TYPE, String.class);
        if (!JwtClaimsKeys.REFRESH.equals(type)) {
            throw new InvalidTokenException(AuthErrorCode.TYPE_ERROR_JWT_TOKEN);
        }

        // Claim 에서 정보 추출
        Long userId = claims.get(AuthConstants.USER_ID_CLAIM_NAME, Long.class);
        String sessionId = claims.get(JwtClaimsKeys.SESSIONID, String.class);
        AuthProvider provider = AuthProvider.valueOf(claims.get(JwtClaimsKeys.PROVIDER, String.class));
        UserRole role = userFacade.getUserById(userId).getRole();

        // Redis 에서 토큰 정보 조회 & 해시 대조
        String tokenId = new TokenId(userId, sessionId).toString();
        Token stored = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new TokenNotFoundException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND_IN_STORE));

        // 저장된 리프레시 토큰 해시와 비교
        String providedHash = tokenHasher.hash(refreshToken);
        if (!providedHash.equals(stored.getRefreshTokenHash())) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // new 세션/토큰 발급
        String newSessionId = jwtTokenProvider.newSessionId();
        String newAT = jwtTokenProvider.generateAccessToken(userId, role, provider, newSessionId);
        String newRT = jwtTokenProvider.generateRefreshToken(userId, provider, newSessionId);

        // 기존 Token 삭제 + 새로운 토큰 정보로 Token 생성 및 저장
        tokenRepository.deleteById(tokenId);
        Token newToken = Token.builder()
                .id(new TokenId(userId, newSessionId).toString())
                .userId(userId)
                .authProvider(provider)
                .refreshTokenHash(tokenHasher.hash(newRT))
                .deviceName(stored.getDeviceName())
                .deviceType(stored.getDeviceType())
                .osType(stored.getOsType())
                .osVersion(stored.getOsVersion())
                .appVersion(stored.getAppVersion())
                .issuedAt(Instant.now())
                .lastUsedAt(Instant.now())
                .build();
        tokenRepository.save(newToken);

        return ReissueTokensRes.builder()
                .accessToken(newAT)
                .refreshToken(newRT)
                .build();
    }

    @Transactional
    public SocialLoginRes issueToken(SocialLoginReq req, final long userId, final RegisterStatus registerStatus) {
        final String sessionId = jwtTokenProvider.newSessionId();
        final String accessToken = jwtTokenProvider.generateAccessToken(
                userId,
                req.role(),
                req.provider(),
                sessionId
        );
        final String refreshToken = jwtTokenProvider.generateRefreshToken(
                userId,
                req.provider(),
                sessionId
        );

        Token token = Token.builder()
                .id(userId + ":" + sessionId)
                .userId(userId)
                .authProvider(req.provider())
                .refreshTokenHash(tokenHasher.hash(refreshToken))
                .deviceName(req.deviceName())
                .deviceType(req.deviceType())
                .osType(req.osType())
                .osVersion(req.osVersion())
                .appVersion(req.appVersion())
                .issuedAt(Instant.now())
                .lastUsedAt(Instant.now())
                .build();
        tokenRepository.save(token);

        return SocialLoginRes.of(accessToken, refreshToken, registerStatus);
    }
}