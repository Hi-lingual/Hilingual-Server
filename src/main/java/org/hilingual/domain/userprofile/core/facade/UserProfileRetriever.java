package org.hilingual.domain.userprofile.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.hilingual.domain.userprofile.api.dto.res.UserProfileResponse;
import org.hilingual.domain.userprofile.core.domain.UserProfile;
import org.hilingual.domain.userprofile.core.exception.UserProfileNotFoundException;
import org.hilingual.domain.userprofile.core.exception.UserProfileCoreErrorCode;
import org.hilingual.domain.userprofile.core.repository.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileRetriever {

    private final UserProfileRepository userProfileRepository;
    private final DiaryRepository diaryRepository;


    public UserProfile findByUserIdOrThrow(final Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(UserProfileCoreErrorCode.USER_PROFILE_NOT_FOUND));
    }

    public UserProfileResponse getUserProfile(final Long userId) {
        UserProfile profile = findByUserIdOrThrow(userId);
        return UserProfileResponse.from(profile);
    }

    public Optional<LocalDateTime> findLatestDiaryCreatedAt(final Long userId) {
        return diaryRepository.findLatestCreatedAtByUserId(userId);
    }

}
