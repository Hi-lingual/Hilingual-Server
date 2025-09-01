package org.sopt.controller.userprofile.service;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.domain.AlarmPreference;
import org.sopt.alarmpreference.facade.AlarmPreferenceFacade;
import org.sopt.alarmpreference.type.AlarmType;
import org.sopt.aws.s3.dto.Purpose;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.userprofile.dto.UserProfileImgReq;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.user.dto.NicknameAvailableRes;
import org.sopt.controller.userprofile.exception.UserProfileSuccessCode;
import org.sopt.controller.userprofile.dto.UserProfileReq;
import org.sopt.controller.userprofile.exception.UserProfileApiErrorCode;
import org.sopt.controller.userprofile.exception.UserProfileImagePurposeMismatchException;
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
    private final AlarmPreferenceFacade alarmPreferenceFacade;
    private final S3Service s3Service;

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
        User user = userFacade.getUserById(userId);
        userProfileFacade.findOptionalByUserId(userId)
                .ifPresent(profile -> {
                    throw new UserProfileAlreadyExistException(UserProfileCoreErrorCode.USER_PROFILE_ALREADY_EXIST);
                });

        String profileImageFileKey = null;
        if (userProfileReq.image() != null) {
            if (userProfileReq.image().purpose() != Purpose.PROFILE_UPDATE) {
                throw new UserProfileImagePurposeMismatchException(UserProfileApiErrorCode.IMAGE_PURPOSE_INVALID);
            }
            profileImageFileKey = s3Service.bindProfileImage(userId, userProfileReq.image().fileKey());
        }

        // fileKey 바인딩 및 UserProfile 저장
        UserProfile userProfile = UserProfile.create(user, userProfileReq.nickname(), profileImageFileKey);
        userProfileFacade.save(userProfile);

        // AlarmPreference에 Marketing 동의 여부 저장
        AlarmPreference alarmPreference = AlarmPreference.create(user, AlarmType.MARKETING, userProfileReq.adAlarmAgree());
        alarmPreferenceFacade.save(alarmPreference);

        // User의 가입 상태 변경
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


    public Void changeUserProfileImg(Long userId, UserProfileImgReq userProfileImgReq) {
        if(userProfileImgReq.image().purpose() != Purpose.PROFILE_UPDATE) {
            throw new UserProfileImagePurposeMismatchException(UserProfileApiErrorCode.IMAGE_PURPOSE_INVALID);
        }

        // 기존 프로필 이미지 키 조회
        String previousFileKey = userProfileFacade.getProfileByUserId(userId).getProfileImg();

        final String fileKey = s3Service.bindProfileImage(userId, userProfileImgReq.image().fileKey());
        userProfileFacade.updateProfileImgByUserId(userId, fileKey);

        // 업로드 성공 시 기존 프로필 이미지 S3에서 삭제
        if (previousFileKey != null && !previousFileKey.isBlank()) {
            s3Service.deleteObject(previousFileKey);
        }

        return null;
    }

}
