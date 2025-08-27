package org.sopt.userprofile.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.dto.UserSearchProjection;
import org.sopt.userprofile.exception.UserProfileNotFoundException;
import org.sopt.userprofile.exception.UserProfileCoreErrorCode;
import org.sopt.userprofile.repository.UserProfileRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileRetriever {

    private final UserProfileRepository userProfileRepository;


    public UserProfile findByUserId(final Long userId) {
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(UserProfileCoreErrorCode.USER_PROFILE_NOT_FOUND));
    }

    public Optional<UserProfile> findOptionalByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public List<UserProfile> findAll() {
        return userProfileRepository.findAll();
    }

    public List<UserProfile> findByUserIds(List<Long> userIds) {
        return userProfileRepository.findByUserIds(userIds);
    }

    public List<UserSearchProjection> findUsersByNickname(Long userId, String keyword, String startKeyword) {
        return userProfileRepository.searchUserListByKeyword(userId, keyword, startKeyword);
    }
}
