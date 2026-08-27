package org.sopt.userprofile.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.exception.InvalidSameNicknameException;
import org.sopt.userprofile.exception.UserProfileCoreErrorCode;
import org.sopt.userprofile.repository.UserProfileRepository;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void incrementTotalDiariesAndRecalculateStreak(Long userId, LocalDate writtenDate, ZoneId userZone) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);
        updateStreakOnWrite(profile, writtenDate, userZone); // 캘린더 상태 기준으로 streak 반영
        resyncTotal(profile);                     // WRITTEN 개수로 동기화
        userProfileRepository.save(profile);
    }

    public String updateNickname(Long userId, String nickname) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);

        if (nickname.equals(profile.getNickname())) {
            throw new InvalidSameNicknameException(UserProfileCoreErrorCode.INVALID_SAME_NICKNAME);
        }

        profile.updateNickname(nickname);
        userProfileRepository.save(profile);

        return profile.getNickname();
    }

    /**
     * 일기를 쓴 순간에 호출 (streak 업데이트)
     */
    public void updateStreakOnWrite(UserProfile profile, LocalDate writtenDate, ZoneId userZone) {
        // 일기 작성 시 타임존 변경된 경우 저장
        profile.getUser().updatePrimaryTimezone(userZone.getId());

        // 전달받은 유저의 로컬 타임존
        LocalDate today     = LocalDate.now(userZone); // D
        LocalDate yesterday = today.minusDays(1); // 어제
        LocalDate dayBeforeYesterday = today.minusDays(2); // 그저께
        LocalDate tomorrow = today.plusDays(1); // 내일

        // 캘린더 사실값 조회
        WriteStatus yStatus = userCalendarFacade.getStatus(profile.getUser(), yesterday);

        int newStreak = profile.getStreak();
        int forwardStreakFromTomorrow = calculateForwardStreakFromDate(profile.getUser(), tomorrow);

        if (writtenDate.equals(today)) {
            if (yStatus == WriteStatus.WRITTEN) {
                // 어제부터의 연속을 사실값으로 재계산 후 +오늘
                int base = calculateStreakFromDate(profile.getUser(), yesterday);
                newStreak = base + 1 + forwardStreakFromTomorrow;
            } else { // 어제 일기가 없는 경우
                WriteStatus dbyStatus = userCalendarFacade.getStatus(profile.getUser(), dayBeforeYesterday);
                if (dbyStatus == WriteStatus.WRITTEN) { // 그저께 일기가 있는 경우
                    newStreak = calculateStreakFromDate(profile.getUser(), dayBeforeYesterday) ;
                } else {
                    // (NONE 또는 DELETED) → 오늘 단독은 즉시 1
                    newStreak = 1 + forwardStreakFromTomorrow;
                }
            }
        } else { // today가 아닌 과거의 어떤 날짜를 보충한 경우
            // 어제부터의 과거
            int pastStreak = calculateStreakFromDate(profile.getUser(), writtenDate.minusDays(1));
            // 오늘부터 미래
            int forwardStreakFromToday = calculateForwardStreakFromDate(profile.getUser(), writtenDate.plusDays(1));

            int continuedDateLength = pastStreak + 1 + forwardStreakFromToday;
            LocalDate endOfChain = writtenDate.plusDays(forwardStreakFromToday);

            if(endOfChain.equals(today) || endOfChain.equals(yesterday)){
                newStreak = continuedDateLength;
            } else { // 최근 일기는 작성하지 않고 과거 일기만 작성한 경우 streak 변화 X
                newStreak = profile.getStreak();
            }
        }
        // 그 외 날짜는 변화 없음

        // 객체 상태만 업데이트 (저장은 호출한 부모 메서드에서 처리)
        profile.updateStreak(newStreak);
    }

    // 특정 날짜부터 과거로 연속 작성 일수를 계산하는 헬퍼 메서드
    private int calculateStreakFromDate(User user, LocalDate startDate) {
        int streak = 0;
        LocalDate cur = startDate;
        while (
                userCalendarFacade.getStatus(user, cur) == WriteStatus.WRITTEN ||
                userCalendarFacade.getStatus(user, cur) == WriteStatus.RECOVERED) {
            streak++;
            cur = cur.minusDays(1);
        }
        return streak;
    }

    // 특정 날짜부터 미래로 연속 작성 일수를 계산하는 헬퍼 메서드
    private int calculateForwardStreakFromDate(User user, LocalDate startDate) {
        int streak = 0;
        LocalDate cur = startDate;
        while (userCalendarFacade.getStatus(user, cur) == WriteStatus.WRITTEN) {
            streak++;
            cur = cur.plusDays(1);
        }
        return streak;
    }

    //특정 날짜(today) 기준 스트릭 계산 (저장 X, read-only)
    public int calculateStreak(final User user, final LocalDate today){
        LocalDate yesterday = today.minusDays(1);
        LocalDate dayBeforeYesterday = today.minusDays(2);
        LocalDate tomorrow = today.plusDays(1);

        WriteStatus dStatus = userCalendarFacade.getStatus(user, today);
        WriteStatus yStatus = userCalendarFacade.getStatus(user, yesterday);
        WriteStatus dbyStatus = userCalendarFacade.getStatus(user, dayBeforeYesterday);

        if(dStatus == WriteStatus.WRITTEN){
            if(yStatus == WriteStatus.WRITTEN){
                return calculateStreakFromDate(user, yesterday) + 1 + calculateForwardStreakFromDate(user, tomorrow);
            } else if (dbyStatus == WriteStatus.WRITTEN) {
                return calculateStreakFromDate(user, dayBeforeYesterday);
            } else {
                return 1 + calculateForwardStreakFromDate(user, tomorrow);
            }
        } else if (yStatus == WriteStatus.WRITTEN) {
            return calculateStreakFromDate(user, yesterday);
        } else if (dbyStatus == WriteStatus.WRITTEN) {
            return calculateStreakFromDate(user, dayBeforeYesterday);
        } else {
            return calculateForwardStreakFromDate(user, tomorrow);
        }
    }

    /**
     * [스케줄러]
     * 매 15분마다 실행, 해당 시간에 자정을 맞이한 타임존 중
     * 스트릭을 잃을 가능성이 있는(streak > 0) 유저만 검사
     */
    @Scheduled(cron = "0 0,15,30,45 * * * *")
    @Transactional
    public void resetStreakIfBrokenGlobal() {
        Instant rawNow = Instant.now();

        // 스케줄러 지연 방어: 현재 시간을 가장 가까운 과거의 15분 단위로 내림 처리
        // ex) 00:01:05 -> 00:00:00 / 00:16:20 -> 00:15:00
        int currentMinute = rawNow.atZone(ZoneOffset.UTC).getMinute();
        int targetMinute = (currentMinute / 15) * 15;

        Instant truncatedNow = rawNow.truncatedTo(ChronoUnit.HOURS)
                .plus(targetMinute, ChronoUnit.MINUTES);

        // 현재 자정을 맞이한 타임존만 추출
        Set<String> midnightZones = ZoneId.getAvailableZoneIds().stream()
                .filter(zone -> {
                    LocalTime localTime = LocalTime.ofInstant(truncatedNow, ZoneId.of(zone));
                    return localTime.getHour() == 0 && localTime.getMinute() == 0;
                })
                .collect(Collectors.toSet());

        if (midnightZones.isEmpty()) {
            return;
        }

        // 해당 타임존이면서 스트릭이 1 이상인 대상만 조회 (streak 0인 대상은 조회할 필요 X)
        List<UserProfile> targetProfiles = userProfileRepository.findTargetsForStreakReset(midnightZones, 0);

        if (targetProfiles.isEmpty()) {
            return;
        }

        // 대상 유저들의 로컬 날짜 기준으로 스트릭 검사 및 갱신
        for (UserProfile profile : targetProfiles) {
            ZoneId userZone = ZoneId.of(profile.getUser().getPrimaryTimezone());

            LocalDate today = LocalDate.ofInstant(truncatedNow, userZone);
            LocalDate yesterday = today.minusDays(1);
            LocalDate dayBeforeYesterday = today.minusDays(2);

            WriteStatus y   = userCalendarFacade.getStatus(profile.getUser(), yesterday);
            WriteStatus dby = userCalendarFacade.getStatus(profile.getUser(), dayBeforeYesterday);

            // 48시간 유예기간이 끝나는 그저께가 기준
            if (dby != WriteStatus.WRITTEN) {
                // 그저께 작성하지 않은 경우 - 오늘부터 이어지는 미래 일기 탐색
                int forwardStreak = calculateForwardStreakFromDate(profile.getUser(), today);

                // 어제 작성하지 않은 경우
                if (y != WriteStatus.WRITTEN) {
                    // 오늘부터 이어지는 미래 일기만 남음
                    profile.updateStreak(forwardStreak);
                } else {
                    // 어제는 씀 - 어제 + 오늘부터 이어지는 미래 일기
                    profile.updateStreak(1 + forwardStreak);
                }
            }
            // 그 외 (dby == WRITTEN) -> 유예기간이 아직 남아있거나 잘 쓰고 있는 상태. 기존 스트릭 안전하게 유지
        }
    }

    /**
     * [스케줄러] 월간 스트릭 부활권 초기화
     * 매 15분마다 실행, 해당 시간에 '매월 1일 자정'을 맞이한 타임존 유저들의
     * 스트릭 부활권(recovery_chance_count)을 3개로 일괄 초기화
     */
    @Scheduled(cron = "0 0,15,30,45 * * * *")
    @Transactional
    public void resetMonthlyRecoveryChanceGlobal() {
        Instant rawNow = Instant.now();

        // 스케줄러 지연 방어: 15분 단위 내림
        int currentMinute = rawNow.atZone(ZoneOffset.UTC).getMinute();
        int targetMinute = (currentMinute / 15) * 15;
        Instant truncatedNow = rawNow.truncatedTo(ChronoUnit.HOURS)
                .plus(targetMinute, ChronoUnit.MINUTES);

        // 현재 시간이 매월 1일이면서 자정인 타임존만 추출
        Set<String> firstDayMidnightZones = ZoneId.getAvailableZoneIds().stream()
                .filter(zone -> {
                    ZonedDateTime zdt = truncatedNow.atZone(ZoneId.of(zone));
                    return zdt.getDayOfMonth() == 1 &&
                            zdt.getHour() == 0 &&
                            zdt.getMinute() == 0;
                })
                .collect(Collectors.toSet());

        if (firstDayMidnightZones.isEmpty()) {
            return;
        }

        // 업데이트 실행
        LocalDateTime now = LocalDateTime.now();
        userProfileRepository.bulkResetRecoveryChanceCount(firstDayMidnightZones, now);
    }

    private void resyncTotal(UserProfile profile) {
        long cnt = userCalendarFacade.countWritten(profile.getUser().getId());
        profile.updateTotalDiaries((int) cnt);
    }

    @Transactional
    public void syncStreak(final long userId, ZoneId userZone) {
        UserProfile profile = userProfileRetriever.findByUserId(userId);
        profile.getUser().updatePrimaryTimezone(userZone.getId());

        LocalDate today = LocalDate.now(userZone);

        int newStreak = calculateStreak(profile.getUser(), today);

        profile.updateStreak(newStreak);
        resyncTotal(profile);
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

    public void decreaseFollowerCountOfFollowees(final long userId) {
        userProfileRepository.decreaseFollowerCountOfFollowees(userId);
    }

    public void decreaseFollowingCountOfFollowers(final long userId) {
        userProfileRepository.decreaseFollowingCountOfFollowers(userId);
    }
}
