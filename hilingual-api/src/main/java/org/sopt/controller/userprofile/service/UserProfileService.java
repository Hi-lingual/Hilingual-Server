package org.sopt.controller.userprofile.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.userprofile.dto.UserProfileReq;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.dto.UserProfileRes;
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

    public UserProfileRes getUserProfile(final Long userId) {
        return userProfileFacade.getUserProfile(userId);
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

        user.setIsCompleted(true);
        userFacade.save(user);
    }
}
