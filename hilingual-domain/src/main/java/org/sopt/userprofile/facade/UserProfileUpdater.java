package org.sopt.userprofile.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.repository.UserProfileRepository;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UserProfileUpdater {

    private final UserCalendarFacade userCalendarFacade;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileRetriever userProfileRetriever;

    /**
     * 일기 작성 시 totalDiaries 증가 및 streak 재계산
     */
    @Transactional
    public void incrementTotalDiariesAndRecalculateStreak(Long userId) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);
        profile.updateTotalDiaries(profile.getTotalDiaries() + 1);
        recalculateStreak(profile);
    }

    /**
     * 일기 삭제 시 totalDiaries 감소 및 streak 재계산
     */
    @Transactional
    public void decrementTotalDiariesAndRecalculateStreak(Long userId) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);
        profile.updateTotalDiaries(profile.getTotalDiaries() - 1);
        recalculateStreak(profile);
    }

    /**
     * 매일 자정, 모든 사용자의 streak 보정 및 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void validateAndRecalculateAllStreaks() {
        List<UserProfile> allProfiles = userProfileRepository.findAll();
        for (UserProfile profile : allProfiles) {
            recalculateStreak(profile);
        }
    }

    /**
     * 오늘부터 과거로 올라가며 streak을 정확히 계산하는 헬퍼 메소드
     */
    private void recalculateStreak(UserProfile profile) {
        int streak = 0;
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Seoul"));

        while(userCalendarFacade.existsByUserAndDate(profile.getUser(), currentDate)) {
            streak ++;
            currentDate = currentDate.minusDays(1);
        }

        profile.updateStreak(streak);
        userProfileRepository.save(profile);
    }

    @Transactional
    public int updateProfileImgByUserId(final long userId, final String newImgUrl, final LocalDateTime updatedAt) {
        return userProfileRepository.updateProfileImgByUserId(userId, newImgUrl, updatedAt);
    }

    public int decrementFollowingCountByUserId(final long userId, final LocalDateTime updatedAt) {
        return userProfileRepository.decrementFollowingCountByUserId(userId, updatedAt);
    }

    public int decrementFollowerCountByUserId(final long userId, final LocalDateTime updatedAt) {
        return userProfileRepository.decrementFollowerCountByUserId(userId, updatedAt);
    }
}