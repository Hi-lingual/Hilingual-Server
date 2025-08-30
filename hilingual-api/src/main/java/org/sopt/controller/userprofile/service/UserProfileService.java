package org.sopt.controller.userprofile.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.dto.Purpose;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.userprofile.dto.UserProfileImgReq;
import org.sopt.controller.userprofile.dto.UserProfileReq;
import org.sopt.controller.userprofile.exception.UserProfileApiErrorCode;
import org.sopt.controller.userprofile.exception.UserProfileImagePurposeMismatchException;
import org.sopt.user.facade.UserFacade;
import org.sopt.user.domain.User;
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
    private final S3Service s3Service;

    public void save(Long userId, UserProfileReq userProfileReq) {
        // TODO : Custom error
        User user = userFacade.getUserById(userId);

        userProfileFacade.findOptionalByUserId(userId)
                .ifPresent(profile -> {
                    throw new UserProfileAlreadyExistException(UserProfileCoreErrorCode.USER_PROFILE_ALREADY_EXIST);
                });

        UserProfile profile = UserProfile.create(user, userProfileReq.nickname(), userProfileReq.profileImg());
        userProfileFacade.save(profile);

        user.setIsCompleted(true);
        userFacade.save(user);
    }

    public Void changeUserProfileImg(Long userId, UserProfileImgReq userProfileImgReq) {
        if(userProfileImgReq.image().purpose() != Purpose.PROFILE_UPDATE) {
            throw new UserProfileImagePurposeMismatchException(UserProfileApiErrorCode.IMAGE_PURPOSE_INVALID);
        }

        final String fileKey = s3Service.bindProfileImage(userId, userProfileImgReq.image().fileKey());
        userProfileFacade.updateProfileImgByUserId(userId, fileKey);

        return null;
    }

}
