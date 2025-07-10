package org.hilingual.domain.user.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.domain.user.api.dto.res.NicknameAvailableResponse;
import org.hilingual.domain.user.api.exception.UserSuccessCode;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.user.core.facade.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRetriever userRetriever;

    public User findById(final long userId) {
        return userRetriever.findByUserId(userId);
    }

    public BaseResponseDto<NicknameAvailableResponse> getNicknameAvailable(String nickname) {
        if (!nickname.matches("^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$")) {
            return unavailableNickname(UserSuccessCode.NICKNAME_SPECIAL_SYMBOLS);
        }
        if (nickname.length() < 2 || nickname.length() > 10) {
            return unavailableNickname(UserSuccessCode.NICKNAME_COUNT);
        }
        if (userRetriever.isNicknameExists(nickname)) {
            return unavailableNickname(UserSuccessCode.NICKNAME_DUPLICATED);
        }
        return availableNickname();
    }

    private BaseResponseDto<NicknameAvailableResponse> availableNickname() {
        return BaseResponseDto.success(UserSuccessCode.NICKNAME_AVAILABLE, new NicknameAvailableResponse(true));
    }

    private BaseResponseDto<NicknameAvailableResponse> unavailableNickname(UserSuccessCode code) {
        return BaseResponseDto.success(code, new NicknameAvailableResponse(false));
    }
}