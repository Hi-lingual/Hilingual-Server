package org.sopt.userprofile.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.repository.UserProfileRepository;
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
        updateStreakOnWrite(userId, writtenDate); // 캘린더 상태 기준으로 streak 반영
        resyncTotal(profile);                     // WRITTEN 개수로 동기화
        userProfileRepository.save(profile);
    }

    /**
     * 일기를 쓴 순간에 호출 (streak 업데이트)
     */
    @Transactional
    public void updateStreakOnWrite(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);

        final ZoneId KST = ZoneId.of("Asia/Seoul");
        LocalDate today     = LocalDate.now(KST);   // D
        LocalDate yesterday = today.minusDays(1);   // Y

        // 캘린더 사실값 조회
        WriteStatus yStatus = userCalendarFacade.getStatus(profile.getUser(), yesterday);
        WriteStatus dStatus = userCalendarFacade.getStatus(profile.getUser(), today);

        int newStreak = profile.getStreak();

        if (writtenDate.equals(today)) {
            if (yStatus == WriteStatus.WRITTEN) {
                // 어제부터의 연속을 사실값으로 재계산 후 +오늘
                int base = calculateStreakFromDate(profile.getUser(), yesterday);
                newStreak = base + 1;
            } else {
                // (NONE 또는 DELETED) → 오늘 단독은 즉시 1
                newStreak = 1;
            }
        } else if (writtenDate.equals(yesterday)) {
            // 어제 보충 시: 어제부터의 연속을 사실값으로 재계산
            int base = calculateStreakFromDate(profile.getUser(), yesterday); // ≥1
            newStreak = base + (dStatus == WriteStatus.WRITTEN ? 1 : 0);
        }
        // 그 외 날짜는 변화 없음

        profile.updateStreak(newStreak);
        userProfileRepository.save(profile);
    }

    // 특정 날짜부터 과거로 연속 작성 일수를 계산하는 헬퍼 메서드
    private int calculateStreakFromDate(User user, LocalDate startDate) {
        int streak = 0;
        LocalDate cur = startDate;
        while (userCalendarFacade.getStatus(user, cur) == WriteStatus.WRITTEN) {
            streak++;
            cur = cur.minusDays(1);
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
        LocalDate yesterday = today.minusDays(1);
        LocalDate dayBeforeYesterday = today.minusDays(2);

        List<UserProfile> all = userProfileRepository.findAll();

        for (UserProfile profile : all) {
            WriteStatus y   = userCalendarFacade.getStatus(profile.getUser(), yesterday);
            WriteStatus dby = userCalendarFacade.getStatus(profile.getUser(), dayBeforeYesterday);

            // (15 X, 16 O) → 1
            if (dby != WriteStatus.WRITTEN && y == WriteStatus.WRITTEN) {
                profile.updateStreak(1);
                continue;
            }
            // (15 X, 16 X) → 0
            if (dby != WriteStatus.WRITTEN && y != WriteStatus.WRITTEN) {
                profile.updateStreak(0);
                continue;
            }
            // (15 O, 16 X) 또는 (15 O, 16 O) → 유지
        }
        userProfileRepository.saveAll(all);
    }

    private void resyncTotal(UserProfile profile) {
        long cnt = userCalendarFacade.countWritten(profile.getUser().getId());
        profile.updateTotalDiaries((int) cnt);
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

    public void decreaseFollowerCountOfFollowees(final long userId) {
        userProfileRepository.decreaseFollowerCountOfFollowees(userId);
    }

    public void decreaseFollowingCountOfFollowers(final long userId) {
        userProfileRepository.decreaseFollowingCountOfFollowers(userId);
    }
}
