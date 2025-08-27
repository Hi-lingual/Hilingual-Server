package org.sopt.controller.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.token.TokenService;
import org.sopt.controller.user.dto.UserDefaultInfoRes;
import org.sopt.controller.user.exception.CannotLoadProviderException;
import org.sopt.controller.user.exception.UserApiErrorCode;
import org.sopt.jwt.auth.dto.ReissueTokensRes;
import org.sopt.user.domain.User;
import org.sopt.controller.user.dto.HomeUserProfileRes;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final TokenService tokenService;

    // TODO : 닉네임 중복 체크 아예 Custom Validator 로 빼자. 현재 UserService 의 책임이 너무 무거움.

    private static final String NICKNAME_PATTERN = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]+$";
    private static final int MIN_NICKNAME_LENGTH = 2;
    private static final int MAX_NICKNAME_LENGTH = 10;

    private final UserFacade userFacade;

    public UserDefaultInfoRes getUserDefaultInfo(final long userId) {
        User user = userFacade.getUserById(userId);

        return UserDefaultInfoRes.from(
                user.getUserProfile(),
                parseProviderInfo(user.getProvider())
        );
    }

    public HomeUserProfileRes getHomeUserInfo(final long userId) {
        User user = userFacade.getUserById(userId);

        return HomeUserProfileRes.from(
                user.getUserProfile(),
                user.getNotifyStatus()
        );
    }

    @Transactional
    public ReissueTokensRes reissue(final String refreshToken) {
        return tokenService.reissue(refreshToken);
    }

    private String parseProviderInfo(String provider) {
        String loginProviderInfo;

        switch (provider) {
            case "GOOGLE":
                loginProviderInfo = "구글 로그인";
                break;
            case "APPLE":
                loginProviderInfo = "애플 로그인";
                break;
            default:
                throw new CannotLoadProviderException(UserApiErrorCode.PROVIDER_LOAD_ERROR);
        }
        return loginProviderInfo;
    }
}
