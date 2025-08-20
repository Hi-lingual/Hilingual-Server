package org.sopt.controller.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopt.controller.block.exception.CannotSelfUnblockException;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.user.dto.UserDefaultInfoRes;
import org.sopt.controller.user.exception.CannotLoadProviderException;
import org.sopt.controller.user.exception.UserApiErrorCode;
import org.sopt.controller.user.exception.UserApiException;
import org.sopt.controller.user.exception.UserSuccessCode;
import org.sopt.dto.BaseResponseDto;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    // TODO : 닉네임 중복 체크 아예 Custom Validator 로 빼자. 현재 UserService 의 책임이 너무 무거움.

    private static final String NICKNAME_PATTERN = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]+$";
    private static final int MIN_NICKNAME_LENGTH = 2;
    private static final int MAX_NICKNAME_LENGTH = 10;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    private final UserFacade userFacade;
    private final UserProfileFacade userProfileFacade;

    public BaseResponseDto<NicknameAvailableRes> getNicknameAvailable(String nickname) {
        if (!isValidFormat(nickname)) {
            return unavailableNickname(UserSuccessCode.NICKNAME_SPECIAL_SYMBOLS);
        }
        if (!isValidLength(nickname)) {
            return unavailableNickname(UserSuccessCode.NICKNAME_COUNT);
        }
        if (userFacade.isNicknameExists(nickname)) {
            return unavailableNickname(UserSuccessCode.NICKNAME_DUPLICATED);
        }
        return availableNickname();
    }

    public UserDefaultInfoRes getUserDefaultInfo(final long userId) {
        UserProfile userProfile = userProfileFacade.getProfileByUserId(userId);
        User user = userFacade.getUserById(userId);

        return new UserDefaultInfoRes(
                userProfile.getProfileImg(),
                userProfile.getNickname(),
                parseProviderInfo(user.getProvider())
        );
    }

    private boolean isValidFormat(String nickname) {
        return nickname.matches(NICKNAME_PATTERN);
    }

    private boolean isValidLength(String nickname) {
        int length = nickname.length();
        return length >= MIN_NICKNAME_LENGTH && length <= MAX_NICKNAME_LENGTH;
    }

    private BaseResponseDto<NicknameAvailableRes> availableNickname() {
        return BaseResponseDto.success(UserSuccessCode.NICKNAME_AVAILABLE, new NicknameAvailableRes(true));
    }

    private BaseResponseDto<NicknameAvailableRes> unavailableNickname(UserSuccessCode code) {
        return BaseResponseDto.success(code, new NicknameAvailableRes(false));
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
