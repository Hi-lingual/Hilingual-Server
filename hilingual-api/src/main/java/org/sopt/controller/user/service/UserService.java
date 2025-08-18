package org.sopt.controller.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.token.TokenService;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.user.exception.UserSuccessCode;
import org.sopt.dto.BaseResponseDto;
import org.sopt.jwt.core.JwtTokenProvider;
import org.sopt.jwt.auth.dto.ReissueTokensRes;
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

    @Transactional
    public ReissueTokensRes reissue(final String refreshToken) {
        return tokenService.reissue(refreshToken);
    }
}
