package org.sopt.controller.userprofile.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.userprofile.exception.UserProfileSuccessCode;
import org.sopt.controller.userprofile.dto.UserProfileReq;
import org.sopt.dto.BaseResponseDto;
import org.sopt.forbiddenword.facade.ForbiddenWordFacade;
import org.sopt.user.facade.UserFacade;
import org.sopt.user.domain.User;
import org.sopt.user.type.RegisterStatus;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.exception.UserProfileAlreadyExistException;
import org.sopt.userprofile.exception.UserProfileCoreErrorCode;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserFacade userFacade;
    private final UserProfileFacade userProfileFacade;
    private final ForbiddenWordFacade forbiddenWordFacade;

    // TODO : 닉네임 중복 체크 아예 Custom Validator 로 빼자

    private static final String NICKNAME_PATTERN = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]+$";
    private static final int MIN_NICKNAME_LENGTH = 2;
    private static final int MAX_NICKNAME_LENGTH = 10;

    public BaseResponseDto<NicknameAvailableRes> getNicknameAvailable(String nickname) {
        if (!isValidFormat(nickname)) {
            return unavailableNickname(UserProfileSuccessCode.NICKNAME_SPECIAL_SYMBOLS);
        }
        if (!isValidLength(nickname)) {
            return unavailableNickname(UserProfileSuccessCode.NICKNAME_COUNT);
        }
        if (userProfileFacade.isNicknameExists(nickname)) {
            return unavailableNickname(UserProfileSuccessCode.NICKNAME_DUPLICATED);
        }
        if (forbiddenWordFacade.findIsInForbiddenWord(nickname)) {
            return unavailableNickname(UserProfileSuccessCode.NICKNAME_FORBIDDEN);
        }
        return availableNickname();
    }

    public void save(Long userId, UserProfileReq userProfileReq) {
        // TODO : Custom error
        User user = userFacade.getUserById(userId);

        userProfileFacade.findOptionalByUserId(userId)
                .ifPresent(profile -> {
                    throw new UserProfileAlreadyExistException(UserProfileCoreErrorCode.USER_PROFILE_ALREADY_EXIST);
                });

        UserProfile profile = UserProfile.create(user, userProfileReq.nickname(), userProfileReq.profileImg());
        userProfileFacade.save(profile);

        user.updateRegisterStatus(RegisterStatus.PROFILE_COMPLETED);
        userFacade.save(user);
    }

    private boolean isValidFormat(String nickname) {
        return nickname.matches(NICKNAME_PATTERN);
    }

    private boolean isValidLength(String nickname) {
        int length = nickname.length();
        return length >= MIN_NICKNAME_LENGTH && length <= MAX_NICKNAME_LENGTH;
    }

    private BaseResponseDto<NicknameAvailableRes> availableNickname() {
        return BaseResponseDto.success(UserProfileSuccessCode.NICKNAME_AVAILABLE, new NicknameAvailableRes(true));
    }

    private BaseResponseDto<NicknameAvailableRes> unavailableNickname(UserProfileSuccessCode code) {
        return BaseResponseDto.success(code, new NicknameAvailableRes(false));
    }

}
