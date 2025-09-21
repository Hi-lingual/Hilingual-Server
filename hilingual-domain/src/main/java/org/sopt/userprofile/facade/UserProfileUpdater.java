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
     * 일기 삭제 시 totalDiaries 감소 및 streak 재계산
     */
    @Transactional
    public void decrementTotalDiariesAndRecalculateStreak(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);
        updateStreakOnDelete(userId, writtenDate); // 삭제 규칙대로 streak 반영
        resyncTotal(profile);                      // WRITTEN 개수로 동기화
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

        int newStreak = profile.getStreak();

        WriteStatus yStatus = userCalendarFacade.getStatus(profile.getUser(), yesterday);
        WriteStatus dStatus = userCalendarFacade.getStatus(profile.getUser(), today);

        if (writtenDate.equals(today)) {
            // 오늘 작성
            if (yStatus == WriteStatus.WRITTEN) {
                newStreak += 1;                 // 정상 연결
            } else if (yStatus == WriteStatus.DELETED) {
                newStreak = 1;   // 하드 미싱 → 오늘 단독 확정 1
            } // NONE이면 보충 가능 → 변화 없음
        } else if (writtenDate.equals(yesterday)) {
            // 어제 보충: +1, 오늘도 WRITTEN이면 +1 추가
            newStreak += 1;
            if (dStatus == WriteStatus.WRITTEN) {
                newStreak += 1;
            }
        }

        profile.updateStreak(newStreak);
        userProfileRepository.save(profile);
    }


    /**
     * 일기를 삭제한 순간에 호출 (streak 업데이트)
     * 규칙 요약:
     *  - 어제(Y) 삭제: 오늘(D)이 있으면 즉시 1, 없으면 0
     *  - 오늘(D) 삭제: 즉시 0 (즉시 단절)
     *  - 그제(DBY) 삭제: 고정값 매핑
     *      Y=O,D=O → 2 / Y=O,D=X → 1 / Y=X,D=O → 1 / Y=X,D=X → 0
     *  - 그 이전(≤DBY-1) 삭제:
     *      Y=O → calc(Y) + (D ? 1 : 0)
     *      Y=X & D=O → min(현재 streak, calc(DBY))
     *      Y=X & D=X → calc(DBY)
     */
    @Transactional
    public void updateStreakOnDelete(Long userId, LocalDate writtenDate) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);

        final ZoneId KST = ZoneId.of("Asia/Seoul");
        LocalDate today = LocalDate.now(KST);         // D (예: 17)
        LocalDate yesterday = today.minusDays(1);     // Y (예: 16)
        LocalDate dby = today.minusDays(2);           // DBY (예: 15)

        WriteStatus dStatus = userCalendarFacade.getStatus(profile.getUser(), today);
        WriteStatus yStatus = userCalendarFacade.getStatus(profile.getUser(), yesterday);

        boolean dWritten = (dStatus == WriteStatus.WRITTEN);
        boolean yWritten = (yStatus == WriteStatus.WRITTEN);

        int newStreak;

        if (writtenDate.equals(today)) {
            // 오늘 삭제 → 즉시 단절
            newStreak = 0;

        } else if (writtenDate.equals(yesterday)) {
            // 어제 삭제 → 오늘 WRITTEN이면 1, 아니면 0
            newStreak = dWritten ? 1 : 0;

        } else if (writtenDate.equals(dby)) {
            // 그제(DBY) 삭제: 고정값
            if (yWritten && dWritten) {
                newStreak = 2;
            } else if (yWritten) {
                newStreak = 1;
            } else if (dWritten) {
                newStreak = 1;
            } else {
                newStreak = 0;
            }

        } else if (writtenDate.isBefore(dby)) {
            // 그 이전(≤DBY-1) 삭제 — 점진 하향, Y의 NONE/DELETED 구분
            if (yWritten) {
                int base = calculateStreakFromDate(profile.getUser(), yesterday);
                newStreak = base + (dWritten ? 1 : 0);
            } else {
                if (yStatus == WriteStatus.DELETED) {
                    // 어제가 하드 미싱이면 오늘 단독만 가능
                    newStreak = dWritten ? 1 : 0;
                } else { // yStatus == NONE (소프트 미싱)
                    int baseFromDBY = calculateStreakFromDate(profile.getUser(), dby);
                    int current = profile.getStreak();
                    newStreak = Math.min(current, baseFromDBY);
                }
            }

        } else {
            // 미래 날짜 등 비정상 입력: 보수적 환산
            int base = calculateStreakFromDate(profile.getUser(), yesterday);
            newStreak = base + (dWritten ? 1 : 0);
        }

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
