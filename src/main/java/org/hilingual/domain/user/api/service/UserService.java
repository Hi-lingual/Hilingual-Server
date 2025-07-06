package org.hilingual.domain.user.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.dto.BaseResponseDto;
import org.hilingual.domain.user.api.dto.res.NicknameAvailableResponse;
import org.hilingual.domain.user.api.exception.UserSuccessCode;
import org.hilingual.domain.user.core.facade.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRetriever userRetriever;

    public BaseResponseDto<NicknameAvailableResponse> getNicknameAvailable(String nickname) {
        // 글자 수 검사
        if (nickname.length() < 2 || nickname.length() > 10) {
            return BaseResponseDto.success(
                    UserSuccessCode.NICKNAME_COUNT,
                    new NicknameAvailableResponse(false)
            );
        }

        // 특수문자/이모지 검사
        if (!isPlainKoreanOrEnglish(nickname)) {
            return BaseResponseDto.success(
                    UserSuccessCode.NICKNAME_SPECIAL_SYMBOLS,
                    new NicknameAvailableResponse(false)
            );
        }

        // 중복 검사
        if (userRetriever.isNicknameExists(nickname)) {
            return BaseResponseDto.success(
                    UserSuccessCode.NICKNAME_DUPLICATED,
                    new NicknameAvailableResponse(false)
            );
        }

        // 사용 가능
        return BaseResponseDto.success(
                UserSuccessCode.NICKNAME_AVAILABLE,
                new NicknameAvailableResponse(true)
        );
    }

    private boolean isPlainKoreanOrEnglish(String nickname) {
        return nickname.matches("^[가-힣a-zA-Z0-9]*$");
    }
}
