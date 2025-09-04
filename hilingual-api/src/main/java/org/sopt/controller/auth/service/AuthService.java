package org.sopt.controller.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.controller.auth.util.AppleKeyService;
import org.sopt.controller.auth.util.ApplePublicKeyList;
import org.sopt.controller.auth.dto.SocialLoginReq;
import org.sopt.controller.auth.dto.SocialLoginRes;
import org.sopt.controller.auth.exception.*;
import org.sopt.controller.auth.util.GoogleOAuth2UserInfo;
import org.sopt.controller.auth.util.MyKeyLocator;
import org.sopt.controller.token.TokenService;
import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.UnAuthorizedException;
import org.sopt.exception.code.GlobalErrorCode;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.user.repository.UserRepository;
import org.sopt.user.type.RegisterStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final UserFacade userFacade;
    private final AppleKeyService appleKeyService;
    private final TaskScheduler taskScheduler;
    private final RedisTemplate<String, String> redisTemplate;

    private static final Integer PROVIDER_TOKEN_MIN_LENGTH = 101;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    public SocialLoginRes socialLogin(String providerToken, SocialLoginReq req) {
        if (providerToken == null || providerToken.length() < PROVIDER_TOKEN_MIN_LENGTH || req.role() != UserRole.USER) {
            throw new UnAuthorizedException(AuthErrorCode.UNAUTHORIZED);
        }

        if (req.provider() == AuthProvider.GOOGLE) {
            return googleLogin(providerToken, req);
        }

        if (req.provider() == AuthProvider.APPLE) {
            return appleLogin(providerToken, req);
        }

        throw new InvalidProviderException(AuthApiErrorCode.INVALID_PROVIDER);
    }

    public Void logout(String accessToken) {
        tokenService.logout(accessToken);
        return null;
    }

    public Void leave(Long userId, String accessToken) {
        // 토큰 삭제 및 무효화
        tokenService.leave(accessToken);

        // User 정보 Soft Delete
        User user = userFacade.getUserById(userId);
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userFacade.save(user);

        // 30일 후 실행되도록 스케줄링
        Runnable unlinkTask = getUnlinkTask(userId);
        Instant scheduledTime = Instant.now().plusSeconds(60);
        taskScheduler.schedule(unlinkTask, scheduledTime);

        return null;
    }

    private Runnable getUnlinkTask(final long userId) {
        // User Provider에 따라 google unlink, apple unlink 스케줄링
        return () -> {
          try {
              // Redis에 임시 보관된 토큰 정보 조회
              String refreshToken = redisTemplate.opsForValue().get("unlink_token:" + userId);
              String provider = redisTemplate.opsForValue().get("unlink_provider:" + userId);

              switch (provider) {
                  case "GOOGLE":
                      googleUnlink(refreshToken);
                      break;
                  case "APPLE":
                      appleUnlink(refreshToken);
                      break;
                  default:
                      log.warn("유저 {}는 소셜 로그인 유저가 아닙니다.", userId);
              }

              // unlink 성공 시 DB에서 사용자 정보 삭제 및 저장해두었던 임시 RefreshToken 삭제
              userFacade.deleteUserById(userId);
              redisTemplate.delete("unlink_token:" + userId);
              redisTemplate.delete("unlink_provider:" + userId);

          } catch (Exception e) {
              log.error("언링크 실패. 유저{}: {}", userId, e.getMessage(), e);
          }
        };
    }

    private void googleUnlink(String refreshToken) {
        WebClient webClient = WebClient.builder().build();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", refreshToken);

        webClient.post()
                .uri("https://accounts.google.com/o/oauth2/revoke")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private void appleUnlink(String refreshToken) {
        String clientSecret = appleKeyService.makeClientSecretToken();

        // revoke API에 전달할 폼 데이터
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", appleKeyService.appleClientId);
        formData.add("client_secret", clientSecret);
        formData.add("token", refreshToken);
        formData.add("token_type_hint", "refresh_token");

        // Apple revoke api 호출
        WebClient webClient = WebClient.builder().build();
        webClient.post()
                .uri("https://appleid.apple.com/auth/oauth2/v2/revoke")
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private SocialLoginRes googleLogin(String providerToken, SocialLoginReq req) {

        GoogleIdToken.Payload payload = verifyGoogleIdentityToken(providerToken);
        if (payload == null) {
            throw new InvalidGoogleTokenException(AuthApiErrorCode.INVALID_GOOGLE_TOKEN);
        }

        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(payload);
        String providerId = userInfo.id();

        return findUserAndIssueToken(providerId, req);
    }

    private SocialLoginRes appleLogin(String providerToken, SocialLoginReq req) {
        if (providerToken == null || providerToken.isEmpty()) {
            throw new UnAuthorizedException(GlobalErrorCode.UNAUTHORIZED);
        }

        Claims claims;
        try {
            MyKeyLocator myKeyLocator = new MyKeyLocator(getPublicKeys()); // 캐시된 키 목록 사용
            claims = Jwts.parser()
                    .keyLocator(myKeyLocator)
                    .build()
                    .parseSignedClaims(providerToken) // Apple ID Token
                    .getPayload();
        } catch (Exception e) {
            // 애플 ID Token 검증 실패
            throw new AppleServerErrorException(AuthApiErrorCode.AUTH_APPLE_SERVER_ERROR);
        }

        String providerId = claims.getSubject();
        return findUserAndIssueToken(providerId, req);
    }

    private SocialLoginRes findUserAndIssueToken(String providerId, SocialLoginReq req) {
        User user;
        Optional<User> optionalUser = userFacade.getByProviderAndProviderId(String.valueOf(req.provider()), providerId);

        // 이미 유저가 존재하는 경우
        if(optionalUser.isPresent()) {
            user = optionalUser.get();

            if(optionalUser.get().getIsDeleted()) {
                // 탈퇴한 회원인 경우 다시 회원 자격 복구
                user.revertDeleteUser();
            }

            // 이미 가입된 유저 토큰 재발급(= 초기 유저와 동일한 로직)
            return tokenService.issueToken(req, user.getId(), user.getRegisterStatus());

        } else {
            user = User.builder()
                    .provider(String.valueOf(req.provider()))
                    .providerId(providerId)
                    .notifyStatus(false)
                    .registerStatus(RegisterStatus.SOCIAL_LOGIN_COMPLETED)
                    .role(UserRole.USER)
                    .build();

            User newUser = userFacade.save(user);
            return tokenService.issueToken(req, newUser.getId(), RegisterStatus.SOCIAL_LOGIN_COMPLETED);
        }
    }

    private GoogleIdToken.Payload verifyGoogleIdentityToken(String idTokenValue) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenValue);

            if (idToken != null) {
                return idToken.getPayload();
            } else {
                // 토큰 형식이 유효하지 않거나 검증에 실패한 경우
                throw new InvalidGoogleTokenException(AuthApiErrorCode.INVALID_GOOGLE_TOKEN);
            }
        } catch (GeneralSecurityException e) {
            // 암호화 관련 보안 예외
            // 클라이언트 ID 불일치, 토큰 위변조 가능성
            throw new InvalidGoogleTokenException(AuthApiErrorCode.INVALID_GOOGLE_TOKEN);
        } catch (IOException e) {
            // 네트워크 통신 예외
            // 구글 서버와 통신 중 발생한 문제
            throw new GoogleServerErrorException(AuthApiErrorCode.AUTH_GOOGLE_SERVER_ERROR);
        }
    }

    /*
     * Apple 공개 키를 JWKS 엔드포인트에서 조회
     */
    @Cacheable(value = "applePublicKeys", key = "'allKeys'") // key = "'allKeys'"를 사용하여 단일 캐시 엔트리로 관리
    public List<ApplePublicKeyList.ApplePublicKey> getPublicKeys() {
        try {
            ApplePublicKeyList response = WebClient.builder()
                    .baseUrl("https://appleid.apple.com")
                    .build()
                    .get()
                    .uri("/auth/keys")
                    .retrieve()
                    .bodyToMono(ApplePublicKeyList.class)
                    .block();

            // Apple JWKS 응답 비어 있는 경우
            if (response == null || response.keys() == null || response.keys().isEmpty()) {
                throw new AppleServerErrorException(AuthApiErrorCode.AUTH_APPLE_SERVER_ERROR);
            }

            return response.keys();
        } catch (Exception e) {
            // 공개 키 로드 실패
            throw new AppleServerErrorException(AuthApiErrorCode.AUTH_APPLE_SERVER_ERROR);
        }
    }
}
