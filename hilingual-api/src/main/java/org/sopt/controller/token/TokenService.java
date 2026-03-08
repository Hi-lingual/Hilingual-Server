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
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

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
            /* TODO [Soft Migration]
                구버전과 신버전 모두 Session id로 하되,
                구버전은 랜덤ID, 신버전은 DeviceUuid로 들어감.
             */
        String identifier = claims.get(JwtClaimsKeys.SESSION_ID, String.class);
        System.out.println("1. 토큰에서 뽑은 identifier: " + identifier); // Redis의 UUID와 똑같은지 확인

        AuthProvider provider = AuthProvider.valueOf(claims.get(JwtClaimsKeys.PROVIDER, String.class));
        UserRole role = userFacade.getUserById(userId).getRole();

        // Redis 에서 토큰 정보 조회 & 해시 대조
        String tokenId = new TokenId(userId, identifier).toString();
        System.out.println("2. DB에서 조회할 tokenId: " + tokenId); // "2:UUID" 형태인지 확인
        Token stored = tokenRepository.findById(tokenId)
                .orElseThrow(() -> new TokenNotFoundException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND_IN_STORE));

        // 저장된 리프레시 토큰 해시와 비교
        String providedHash = tokenHasher.hash(refreshToken);
        if (!providedHash.equals(stored.getRefreshTokenHash())) {
            throw new InvalidTokenException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        // new 세션/토큰 발급
        String newAT = jwtTokenProvider.generateAccessToken(userId, role, provider, identifier);
        String newRT = jwtTokenProvider.generateRefreshToken(userId, provider, identifier);

        // 기존 Token 삭제 + 새로운 토큰 정보로 Token 생성 및 저장
        tokenRepository.deleteById(tokenId);
        Token newToken = Token.builder()
                .id(tokenId)
                .userId(userId)
                .authProvider(provider)
                .refreshTokenHash(tokenHasher.hash(newRT))
                .deviceUuid(stored.getDeviceUuid())   // 신버전용
                .deviceName(stored.getDeviceName())   // 레거시용 유지
                .deviceType(stored.getDeviceType())   // 레거시용 유지
                .osType(stored.getOsType())           // 레거시용 유지
                .osVersion(stored.getOsVersion())     // 레거시용 유지
                .appVersion(stored.getAppVersion())   // 레거시용 유지
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
        boolean isNewApp = StringUtils.hasText(req.deviceUuid());
        String identifier;
        Token token;

        if (isNewApp) {
            identifier = req.deviceUuid();
            token = Token.create(
                    userId,
                    req.provider(),
                    null, // 임시
                    identifier
            );
        } else {
            identifier = jwtTokenProvider.newSessionId();
            token = Token.createLegacy(
                    userId,
                    req.provider(),
                    null, // 임시
                    identifier,
                    req.deviceName(),
                    req.deviceType(),
                    req.osType(),
                    req.osVersion(),
                    req.appVersion()
            );
        }


        final String accessToken = jwtTokenProvider.generateAccessToken(
                userId,
                req.role(),
                req.provider(),
                identifier
        );
        final String refreshToken = jwtTokenProvider.generateRefreshToken(
                userId,
                req.provider(),
                identifier
        );

        token.updateRefreshTokenHash(tokenHasher.hash(refreshToken));
        tokenRepository.save(token);

        return SocialLoginRes.of(accessToken, refreshToken, registerStatus, userId);
    }

    @Transactional
    public void logout(final String accessToken) {
        Claims claims = getClaimsFromAccessToken(accessToken);

        // Claim 에서 정보 추출
        Long userId = claims.get(AuthConstants.USER_ID_CLAIM_NAME, Long.class);
        String identifier = claims.get(JwtClaimsKeys.SESSION_ID, String.class);

        // Redis 에서 기존 토큰 삭제
        String tokenId = new TokenId(userId, identifier).toString();
        tokenRepository.deleteById(tokenId);
    }

    @Transactional
    public void leave(final String accessToken) {
        Claims claims = getClaimsFromAccessToken(accessToken);

        // Claim 에서 정보 추출
        Long userId = claims.get(AuthConstants.USER_ID_CLAIM_NAME, Long.class);
        List<Token> tokens = tokenRepository.findByUserId(userId);

        // 기존의 모든 RefreshToken을 Redis에서 삭제
        List<String> tokenIds = tokens.stream().map(Token::getId).toList();
        tokenRepository.deleteAllById(tokenIds);
    }

    private Claims getClaimsFromAccessToken(final String accessToken) {
        Claims claims = jwtTokenProvider.parseAndVerify(accessToken);
        final String type = claims.get(JwtClaimsKeys.TYPE, String.class);

        if (!JwtClaimsKeys.ACCESS.equals(type)) {
            throw new InvalidTokenException(AuthErrorCode.TYPE_ERROR_JWT_TOKEN);
        }
        return claims;
    }

}