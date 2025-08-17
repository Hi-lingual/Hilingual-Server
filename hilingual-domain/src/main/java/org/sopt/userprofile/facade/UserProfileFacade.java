package org.sopt.userprofile.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.dto.UserProfileRes;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public UserProfileRes getUserProfile(Long userId) {
        return userProfileRetriever.getUserProfile(userId);
    }

    public Optional<UserProfile> findOptionalByUserId(Long userId) {
        return userProfileRetriever.findOptionalByUserId(userId);
    }

    public List<UserProfile> findAll() {
        return userProfileRetriever.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserProfile> getProfilesByUserIds(List<Long> userIds) {
        return userProfileRetriever.findByUserIds(userIds);
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
    public void incrementTotalDiaries(Long userId) {
        userProfileUpdater.incrementTotalDiaries(userId);
    }

    public void updateStreakOnWrite(Long userId, LocalDate writtenDate) {
        userProfileUpdater.updateStreakOnWrite(userId, writtenDate);
    }

}
