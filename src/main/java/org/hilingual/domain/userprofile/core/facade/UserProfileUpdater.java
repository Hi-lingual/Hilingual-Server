package org.hilingual.domain.userprofile.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.usercalendar.core.repository.UserCalendarRepository;
import org.hilingual.domain.userprofile.core.domain.UserProfile;
import org.hilingual.domain.userprofile.core.repository.UserProfileRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUpdater {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileRetriever  userProfileRetriever;
    private final UserCalendarRepository userCalendarRepository;

    /**
     * totalDiaries 증가
     */
    @Transactional
    public void incrementTotalDiaries(Long userId) {
        UserProfile profile = userProfileRetriever.findByUserIdOrThrow(userId);
        profile.updateTotalDiaries(profile.getTotalDiaries() + 1);
        userProfileRepository.save(profile);
    }

    /**
     * 1. 일기를 쓴 순간에 호출 (streak 업데이트)
     */
    @Transactional
    public void updateStreakOnWrite(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserIdOrThrow(userId);

        LocalDate today     = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate yesterday = today.minusDays(1);

        int newStreak = profile.getStreak();

        boolean hasYesterday = userCalendarRepository
                .existsByUserAndDate(profile.getUser(), yesterday);
        boolean hasToday     = userCalendarRepository
                .existsByUserAndDate(profile.getUser(), today);

        if (writtenDate.equals(today)) {
            // 1) 오늘 쓰는 순간 → 어제 썼다면 +1, 아니면 유지
            if (hasYesterday) {
                newStreak++;
            } else if (profile.getStreak() == 0) {
                newStreak = 1; // 최초작성이거나 작성 재개
        }
        else if (writtenDate.equals(yesterday)) {
            // 2) 어제 보충 쓰는 순간 → 무조건 +1
            newStreak++;
            //    + 오늘도 이미 쓰여 있으면 추가 +1
            if (hasToday) {
                newStreak++;
            } if (profile.getStreak() == 0) {
            newStreak = 1;} // 최초작성이거나 작성 재개
            }

        }
        // 그 외 날짜: 변경 없음
        profile.updateStreak(newStreak);
        userProfileRepository.save(profile);
    }

    /**
     * 2. 매일 자정 최근 2일 작성 여부 검사 후 streak 리셋 또는 유지
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetStreakIfBroken() {
        LocalDate today     = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate dayBefore = today.minusDays(2);

        List<UserProfile> all = userProfileRepository.findAll();
        for (UserProfile profile : all) {
            // 그제 작성 여부를 확인
            boolean hasDayBefore = userCalendarRepository
                    .existsByUserAndDate(profile.getUser(), dayBefore);

            // 그제가 비어 있으면 streak 리셋, 아니면 유지
            if (!hasDayBefore) {
                profile.updateStreak(0);
            }
        }
        userProfileRepository.saveAll(all);
    }
}
