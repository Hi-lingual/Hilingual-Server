package org.sopt.auth.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.sopt.client.apple.MyKeyLocator;
import org.sopt.client.apple.dto.ApplePublicKeyResponse;
import org.sopt.client.apple.dto.AppleTokenResponse;
import org.sopt.client.google.dto.GoogleOAuth2UserInfo;
import org.sopt.constant.AuthConstants;
import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.GoogleAuthUnAuthorizedException;
import org.sopt.exception.code.GlobalErrorCode;
import org.sopt.jwt.auth.JwtProvider;
import org.sopt.jwt.auth.RefreshTokenService;
import org.sopt.jwt.dto.res.JwtTokenResponse;
import org.sopt.jwt.exception.UnauthorizedException;
import org.sopt.user.domain.User;
import org.sopt.user.repository.UserRepository;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String appleClientId;

    @Value("${apple.oauth.key-id}")
    private String appleKeyId;

    @Value("${apple.oauth.team-id}")
    private String appleTeamId;

    @Value("${apple.oauth.private-key-value}")
    private String applePrivateKey;

    private static final long THIRTY_DAYS_MS = 1000L * 60 * 60 * 24 * 30;

    @Transactional
    public JwtTokenResponse socialLogin(String provider, String providerToken) {
        if (providerToken == null || providerToken.isEmpty()) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        if (AuthConstants.PROVIDER_GOOGLE.equalsIgnoreCase(provider)) {
            return googleLogin(providerToken);
        } else if (AuthConstants.PROVIDER_APPLE.equalsIgnoreCase(provider)) {
            return appleLogin(providerToken);
        } else {
            log.error("[소셜 로그인] 지원하지 않는 Provider: {}", provider);
            throw new UnauthorizedException(GlobalErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @Transactional
    public JwtTokenResponse googleLogin(String providerToken) {
        if (providerToken == null || providerToken.length() < 101) {
            throw new GoogleAuthUnAuthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        GoogleIdToken.Payload payload = verifyGoogleIdToken(providerToken);
        if (payload == null) {
            log.info("[구글 로그인] id token 오류");
            throw new GoogleAuthUnAuthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(payload);
        String providerId = userInfo.getId();

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(AuthConstants.PROVIDER_GOOGLE, providerId);
        User user;

        log.info("[구글 로그인] provider, providerId에 해당하는 유저 있는지 확인 : {}", optionalUser);
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            // TODO 만약 탈퇴한 유저(isDeleted true인 경우) 다시 활성화시켜주는 로직

            updateIsCompletedStatus(user);
        } else {
            user = User.builder()
                    .provider(AuthConstants.PROVIDER_GOOGLE)
                    .providerId(providerId)
                    .build();
            log.info("[구글 로그인] 새로 생성된 유저 : {}", user);
            userRepository.save(user);
        }

        JwtTokenResponse authToken = jwtProvider.generateToken(user.getId());
        refreshTokenService.save(user.getId(), authToken.getRefreshToken());
        updateIsCompletedStatus(user);

        return JwtTokenResponse.of(authToken.getAccessToken(), authToken.getRefreshToken(), user.getIsCompleted());
    }

    private GoogleIdToken.Payload verifyGoogleIdToken(String idTokenValue) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
        log.info("[구글 로그인] id token은 {}", idTokenValue);
        log.info("[구글 로그인] Google Client ID (audience)는 {}", googleClientId);

        try {
            GoogleIdToken idToken = verifier.verify(idTokenValue);
            if (idToken != null) {
                log.info("[구글 로그인] ID Token 검증 성공: {}", idToken.getPayload().getSubject());
                return idToken.getPayload();
            } else {
                log.warn("[구글 로그인] ID Token 검증 실패: verifier.verify(idTokenValue)가 null 반환");
                throw new GoogleAuthUnAuthorizedException(GlobalErrorCode.UNAUTHORIZED);
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new GoogleAuthUnAuthorizedException(AuthErrorCode.AUTH_GOOGLE_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateIsCompletedStatus(User user) {
        Optional<UserProfile> userProfileOptional = userProfileRepository.findByUser(user);
        boolean profileExists = userProfileOptional.isPresent();

        if (user.getIsCompleted() != profileExists) {
            user.setIsCompleted(profileExists);
            userRepository.save(user);
            log.info("[isCompleted 업데이트] User ID: {}, isCompleted: {}", user.getId(), user.getIsCompleted());
        } else {
            log.info("[isCompleted 업데이트] User ID: {}, isCompleted 상태 변경 없음: {}", user.getId(), user.getIsCompleted());
        }
    }

    public AppleTokenResponse getAppleToken(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://appleid.apple.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        try {
            return webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/auth/token")
                            .queryParam("grant_type", "authorization_code")
                            .queryParam("client_id", appleClientId)
                            .queryParam("client_secret", makeClientSecretToken())
                            .queryParam("code", code)
                            .build())
                    .retrieve()
                    .bodyToMono(AppleTokenResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("[애플 로그인 실패]: {}", e.getResponseBodyAsString(), e);
            throw e;
        }
    }

    public String makeClientSecretToken() {
        String token = Jwts.builder()
                .subject(appleClientId) // sub (Service ID / Client ID)
                .issuer(appleTeamId) // iss (Team ID)
                .issuedAt(new Date()) // iat
                .expiration(new Date(System.currentTimeMillis() + THIRTY_DAYS_MS)) // exp (최대 6개월)
                .audience() // <--- 인자 없이 호출
                .add("https://appleid.apple.com") // <--- AudienceBuilder에 add()
                .and() // <--- 다시 JwtBuilder로 돌아감
                .header() // 헤더 빌더 시작
                .keyId(appleKeyId) // kid (Key ID) 설정
                .and() // 다시 JWT 빌더로 돌아감
                .signWith(getPrivateKey(), Jwts.SIG.ES256) // 개인 키로 서명 (JJWT 0.12.0+ 필요)
                .compact();
        log.info("[애플 로그인] 로그인 요청 인증 토큰: {}", token);
        return token;
    }

    private PrivateKey getPrivateKey() {
        try {
            byte[] decodedKeyBytes = Base64.getDecoder().decode(applePrivateKey);
            String pemKeyContent = new String(decodedKeyBytes, StandardCharsets.UTF_8);

            PEMParser pemParser = new PEMParser(new StringReader(pemKeyContent));
            Object object = pemParser.readObject();

            if (object instanceof PrivateKeyInfo) {
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                return converter.getPrivateKey((PrivateKeyInfo) object);
            } else {
                log.error("[애플 로그인] 예상치 못한 Private Key 형식: {}", object.getClass().getName());
                throw new RuntimeException("애플 로그인 실패: 개인 키 파싱 실패 - 예상치 못한 형식");
            }
        } catch (Exception e) {
            log.error("[애플 로그인] PK 생성 실패: {}", e.getMessage(), e);
            throw new RuntimeException("애플 로그인 실패: 개인 키 로드 중 오류 발생", e);
        }
    }

    @Cacheable(value = "applePublicKeys", key = "'allKeys'") // key = "'allKeys'"를 사용하여 단일 캐시 엔트리로 관리
    public List<ApplePublicKeyResponse.ApplePublicKeyDto> getPublicKeys() {
        log.info("[애플 로그인] Apple 공개 키를 JWKS 엔드포인트에서 가져옵니다.");
        try {
            ApplePublicKeyResponse response = WebClient.builder()
                    .baseUrl("https://appleid.apple.com")
                    .build()
                    .get()
                    .uri("/auth/keys")
                    .retrieve()
                    .bodyToMono(ApplePublicKeyResponse.class)
                    .block();

            if (response == null || response.getKeys() == null || response.getKeys().isEmpty()) {
                throw new RuntimeException("[애플 로그인] Apple JWKS 응답이 비어있습니다.");
            }
            log.info("[애플 로그인] Apple 공개 키 {}개 성공적으로 로드 및 캐싱됨.", response.getKeys().size());
            return response.getKeys();
        } catch (Exception e) {
            log.error("[애플 로그인] Apple 공개 키 로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("[애플 로그인] Apple 공개 키 로드 중 오류 발생", e);
        }
    }


    @Transactional
    public JwtTokenResponse appleLogin(String providerToken) {
        if (providerToken == null || providerToken.isEmpty()) {
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        Claims claims;
        try {
            MyKeyLocator myKeyLocator = new MyKeyLocator(getPublicKeys()); // 캐시된 키 목록 사용
            claims = Jwts.parser()
                    .keyLocator(myKeyLocator)
                    .build()
                    .parseSignedClaims(providerToken) // Apple ID Token
                    .getPayload();
            log.info("[애플 로그인] idToken 검증 완료: {}", claims.toString());
        } catch (Exception e) {
            log.error("[애플 로그인] ID Token 검증 실패: {}", e.getMessage(), e);
            throw new UnauthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        String authId = claims.getSubject();

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(AuthConstants.PROVIDER_APPLE, authId);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            // TODO 만약 탈퇴한 유저(isDeleted true인 경우) 다시 활성화시켜주는 로직
        } else {
            user = User.builder()
                    .provider(AuthConstants.PROVIDER_APPLE)
                    .providerId(authId)
                    .isCompleted(false)
                    .build();
            log.info("[애플 로그인] 새로 생성된 유저 : {}", user);
            userRepository.save(user);
        }

        JwtTokenResponse authToken = jwtProvider.generateToken(user.getId());
        refreshTokenService.save(user.getId(), authToken.getRefreshToken());

        updateIsCompletedStatus(user);

        return JwtTokenResponse.of(authToken.getAccessToken(), authToken.getRefreshToken(), user.getIsCompleted());
    }
}
