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
    public void incrementTotalDiariesAndRecalculateStreak(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);
        profile.updateTotalDiaries(profile.getTotalDiaries() + 1);
        updateStreakOnWrite(userId, writtenDate);
        userProfileRepository.save(profile);
    }

    /**
     * 일기 삭제 시 totalDiaries 감소 및 streak 재계산
     */
    @Transactional
    public void decrementTotalDiariesAndRecalculateStreak(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);
        profile.updateTotalDiaries(profile.getTotalDiaries() - 1);
        updateStreakOnDelete(userId, writtenDate);
        userProfileRepository.save(profile);
    }

    /**
     * 일기를 쓴 순간에 호출 (streak 업데이트)
     */
    @Transactional
    public void updateStreakOnWrite(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);

        LocalDate today     = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate yesterday = today.minusDays(1);

        int newStreak = profile.getStreak();

        boolean hasYesterday = userCalendarFacade.existsByUserAndDate(profile.getUser(), yesterday);
        boolean hasToday = userCalendarFacade.existsByUserAndDate(profile.getUser(), today);

        if (writtenDate.equals(today)) {
            // 오늘 쓰는 순간 → 어제 썼다면 +1, 아니면 새로 시작
            if (hasYesterday) {
                newStreak++;
            } else if (profile.getStreak() == 0) {
                newStreak = 1; // 최초 작성이거나 작성 재개
            }
        } else if (writtenDate.equals(yesterday)) {
            // 어제 보충 작성 → 무조건 +1
            newStreak++;
            // 오늘도 이미 쓰여 있으면 추가 +1
            if (hasToday) {
                newStreak++;
            }
            if (profile.getStreak() == 0) {
                newStreak = 1; // 최초 작성이거나 작성 재개
            }
        }

        profile.updateStreak(newStreak);
        userProfileRepository.save(profile);
    }

    /**
     * 일기를 삭제한 순간에 호출 (streak 업데이트)
     */
    public void updateStreakOnDelete(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate yesterday = today.minusDays(1);
        LocalDate dayBeforeYesterday = today.minusDays(2);

        int newStreak;

        // 그저께까지의 streak을 정확히 계산
        int streakUpToDayBeforeYesterday = calculateStreakFromDate(profile.getUser(), dayBeforeYesterday);

        // 48시간 유예 기간을 고려하여 streak 업데이트
        boolean hasYesterday = userCalendarFacade.existsByUserAndDate(profile.getUser(), yesterday);
        boolean hasToday = userCalendarFacade.existsByUserAndDate(profile.getUser(), today);

        if (hasYesterday && hasToday) {
            newStreak = streakUpToDayBeforeYesterday + 2;
        } else if (hasYesterday) {
            newStreak = streakUpToDayBeforeYesterday + 1;
        } else if (hasToday) {
            newStreak = streakUpToDayBeforeYesterday;
        } else {
            newStreak = 0;
        }

        profile.updateStreak(newStreak);
        userProfileRepository.save(profile);
    }

    // 특정 날짜부터 과거로 연속 작성 일수를 계산하는 헬퍼 메서드
    private int calculateStreakFromDate(User user, LocalDate startDate) {
        int streak = 0;
        LocalDate currentDate = startDate;

        while (userCalendarFacade.existsByUserAndDate(user, currentDate)) {
            streak++;
            currentDate = currentDate.minusDays(1);
        }
        return streak;
    }

    /**
     * 매일 자정 → 최근 2일 작성 여부 검사 후 streak 리셋 or 유지
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetStreakIfBroken() {
        LocalDate today     = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate dayBefore = today.minusDays(2);

        List<UserProfile> all = userProfileRepository.findAll();

        for (UserProfile profile : all) {
            // 그제 작성 여부 확인
            boolean hasDayBefore = userCalendarFacade
                    .existsByUserAndDate(profile.getUser(), dayBefore);
            if (!hasDayBefore) {
                // 그제 안 썼으면 streak 리셋
                profile.updateStreak(0);
            }
        }
        userProfileRepository.saveAll(all);
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