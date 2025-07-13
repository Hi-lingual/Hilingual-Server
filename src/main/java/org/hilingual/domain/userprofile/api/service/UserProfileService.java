package org.hilingual.domain.userprofile.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.GlobalErrorCode;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.user.core.exception.UserNotFoundException;
import org.hilingual.domain.user.core.repository.UserRepository;
import org.hilingual.domain.userprofile.api.dto.req.UserProfileRequest;
import org.hilingual.domain.userprofile.api.dto.res.UserProfileResponse;
import org.hilingual.domain.userprofile.core.domain.UserProfile;
import org.hilingual.domain.userprofile.core.exception.UserProfileAlreadyExistException;
import org.hilingual.domain.userprofile.core.exception.UserProfileCoreErrorCode;
import org.hilingual.domain.userprofile.core.facade.UserProfileRetriever;
import org.hilingual.domain.userprofile.core.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileRetriever userProfileRetriever;

    public UserProfileResponse getUserProfile(final Long userId) {
        return userProfileRetriever.getUserProfile(userId);
    }

    public void save(Long userId, UserProfileRequest userProfileRequest) {
        // TODO Custom error
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(GlobalErrorCode.INVALID_INPUT_VALUE));

        Optional<UserProfile> existingProfileOpt = userProfileRepository.findByUserId(userId);
        if (existingProfileOpt.isPresent()) {
            throw new UserProfileAlreadyExistException(UserProfileCoreErrorCode.USER_PROFILE_ALREADY_EXIST);
        }

        UserProfile profile = UserProfile.create(user, userProfileRequest.nickname(), userProfileRequest.profileImg());
        userProfileRepository.save(profile);

        user.setIsCompleted(true);
        userRepository.save(user);
    }
}
