package org.hilingual.auth.api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hilingual.auth.core.domain.GoogleOAuth2UserInfo;
import org.hilingual.auth.api.exception.AuthErrorCode;
import org.hilingual.auth.core.exception.GoogleAuthException;
import org.hilingual.domain.token.api.dto.res.JwtTokenResponse;
import org.hilingual.domain.token.api.service.JwtProvider;
import org.hilingual.domain.token.api.service.RefreshTokenService;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.user.core.repository.UserRepository;
import org.hilingual.domain.usesrprofile.UserProfile;
import org.hilingual.domain.usesrprofile.core.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

import static org.hilingual.auth.api.constant.AuthConstants.PROVIDER_GOOGLE;

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

    @Transactional
    public JwtTokenResponse googleLogin(String provider, String providerToken) {
        if(!PROVIDER_GOOGLE.equalsIgnoreCase(provider)) {
            log.info("[구글 로그인] -> 안돼요");
            throw new GoogleAuthException(AuthErrorCode.AUTH_GOOGLE_SERVER_ERROR);
        }

        GoogleIdToken.Payload payload = verifyGoogleIdToken(providerToken);
        if (payload == null) {
            log.info("[구글 로그인] id token 오류");
            throw new GoogleAuthException(AuthErrorCode.INVALID_GOOGLE_ID_TOKEN);
        }

        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(payload);
        String providerId = userInfo.getId();

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(PROVIDER_GOOGLE, providerId);
        User user;

        log.info("[구글 로그인] provider, providerId에 해당하는 유저 있는지 확인 : {}", optionalUser);
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            // TODO 만약 탈퇴한 유저(isDeleted true인 경우) 다시 활성화시켜주는 로직

            updateIsCompletedStatus(user);
        } else {
            user = User.builder()
                    .provider(provider)
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
                return null;
            }
        } catch (GeneralSecurityException | IOException e) {
            throw new GoogleAuthException(AuthErrorCode.AUTH_GOOGLE_SERVER_ERROR);
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

}
