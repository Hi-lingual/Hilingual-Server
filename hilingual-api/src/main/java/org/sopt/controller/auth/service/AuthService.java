package org.sopt.controller.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.auth.dto.SocialLoginReq;
import org.sopt.controller.auth.dto.SocialLoginRes;
import org.sopt.controller.auth.exception.AuthApiErrorCode;
import org.sopt.controller.auth.exception.GoogleServerErrorException;
import org.sopt.controller.auth.exception.InvalidGoogleTokenException;
import org.sopt.controller.token.TokenService;
import org.sopt.exception.AuthErrorCode;
import org.sopt.exception.UnAuthorizedException;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.TokenRepository;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.core.TokenHasher;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.user.type.RegisterStatus;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final UserFacade userFacade;
    private final UserProfileFacade userProfileFacade;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final TokenHasher tokenHasher;

    private static final Integer PROVIDER_TOKEN_MIN_LENGTH = 101;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    public SocialLoginRes socialLogin(String providerToken, SocialLoginReq req) {
        if (providerToken == null || providerToken.length() < PROVIDER_TOKEN_MIN_LENGTH || req.role() != UserRole.USER) {
            throw new UnAuthorizedException(AuthErrorCode.UNAUTHORIZED);
        }

        GoogleIdToken.Payload payload = verifyGoogleIdentityToken(providerToken);
        if (payload == null) {
            throw new InvalidGoogleTokenException(AuthApiErrorCode.INVALID_GOOGLE_TOKEN);
        }

        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(payload);
        String providerId = userInfo.id();

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
}
