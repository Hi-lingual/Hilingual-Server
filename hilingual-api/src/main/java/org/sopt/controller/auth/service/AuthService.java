package org.sopt.controller.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.alarmpreference.facade.AlarmPreferenceFacade;
import org.sopt.alarmpreference.facade.AlarmPreferenceRemover;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.auth.util.AppleKeyService;
import org.sopt.controller.auth.util.ApplePublicKeyList;
import org.sopt.controller.auth.dto.SocialLoginReq;
import org.sopt.controller.auth.dto.SocialLoginRes;
import org.sopt.controller.auth.exception.*;
import org.sopt.controller.auth.util.GoogleOAuth2UserInfo;
import org.sopt.controller.auth.util.MyKeyLocator;
import org.sopt.controller.token.TokenService;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.UnAuthorizedException;
import org.sopt.exception.code.GlobalErrorCode;
import org.sopt.feedalarm.facade.FeedAlarmFacade;
import org.sopt.feedalarm.facade.FeedAlarmRemover;
import org.sopt.follow.facade.FollowFacade;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.likeddiary.facade.LikedDiaryFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.user.type.RegisterStatus;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.voca.facade.VocaFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final UserFacade userFacade;
    private final UserCalendarFacade userCalendarFacade;
    private final DiaryFacade diaryFacade;
    private final LikedDiaryFacade likedDiaryFacade;
    private final FeedAlarmFacade feedAlarmFacade;
    private final BlockFacade blockFacade;
    private final FollowFacade followFacade;
    private final AlarmPreferenceFacade alarmPreferenceFacade;
    private final VocaFacade vocaFacade;

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

    @Transactional
    public Void leave(Long userId, String accessToken) {
        // 토큰 삭제 및 무효화
        tokenService.leave(accessToken);

        // User 정보 Hard Delete
        userCalendarFacade.deleteAllByUserId(userId); // userCalendar 정보 삭제
        likedDiaryFacade.deleteAllByUserId(userId); // LikedDiary 삭제
        vocaFacade.deleteAllByUserId(userId); // Voca 삭제
        diaryFacade.deleteAllByUserId(userId); // Diary, Recommend, DiaryFeedback 삭제
        feedAlarmFacade.deleteAllByUserId(userId); // FeedAlarm 삭제
        blockFacade.deleteAllByUserId(userId); // Block 삭제
        followFacade.deleteAllByUserId(userId); // Follow 삭제
        alarmPreferenceFacade.deleteAllByUserId(userId); // AlarmPreference 삭제

        userFacade.deleteUserById(userId); // user, userProfile, noticeDelivery 삭제

        return null;
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
