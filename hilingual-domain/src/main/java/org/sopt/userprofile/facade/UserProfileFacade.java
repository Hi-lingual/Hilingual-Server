package org.sopt.userprofile.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.dto.UserSearchProjection;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileFacade {

    private final UserProfileRetriever userProfileRetriever;
    private final UserProfileUpdater userProfileUpdater;
    private final UserProfileSaver userProfileSaver;

    /**
    retriever
     */
    public UserProfile findByUserId(Long userId) {
        return userProfileRetriever.findByUserId(userId);
    }

    public Optional<UserProfile> findOptionalByUserId(Long userId) {
        return userProfileRetriever.findOptionalByUserId(userId);
    }

    public List<UserProfile> findAll() {
        return userProfileRetriever.findAll();
    }

    public List<UserProfile> getProfilesByUserIds(List<Long> userIds) {
        return userProfileRetriever.findByUserIds(userIds);
    }

    public UserProfile getProfileByUserId(Long userId) {
        return userProfileRetriever.findByUserId(userId);
    }


    public boolean isNicknameExists(final String nickname) {
        return userProfileRetriever.isNicknameExists(nickname);
    }

    public List<UserSearchProjection> getUserListByNickname(Long userId, String keyword, String startKeyword) {
        return userProfileRetriever.findUsersByNickname(userId, keyword, startKeyword);
    }

    /**
    saver
     */
    public UserProfile save(UserProfile userProfile) {
        return userProfileSaver.save(userProfile);
    }

    public List<UserProfile> saveAll(List<UserProfile> profiles) {
        return userProfileSaver.saveAll(profiles);
    }

    /**
    updater
     */
    public void incrementTotalDiariesAndRecalculateStreak(final long userId, final LocalDate writtenDate) {
        userProfileUpdater.incrementTotalDiariesAndRecalculateStreak(userId, writtenDate);
    }

    public void decrementTotalDiariesAndRecalculateStreak(final long userId, final LocalDate writtenDate) {
        userProfileUpdater.decrementTotalDiariesAndRecalculateStreak(userId, writtenDate);
    }

    public int updateProfileImgByUserId(final long userId, final String newImgUrl, final LocalDateTime updatedAt) {
        return userProfileUpdater.updateProfileImgByUserId(userId, newImgUrl, updatedAt);
    }

    public int decrementFollowingCountByUserId(final long userId, final LocalDateTime updatedAt) {
        return userProfileUpdater.decrementFollowingCountByUserId(userId, updatedAt);
    }

    public int decrementFollowerCountByUserId(final long userId, final LocalDateTime updatedAt) {
        return userProfileUpdater.decrementFollowerCountByUserId(userId, updatedAt);
    }
}
