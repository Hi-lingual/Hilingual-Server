package org.sopt.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.device.domain.Device;
import org.sopt.device.facade.DeviceFacade;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.repository.UserProfileRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class StreakReminderScheduler {

    private final UserCalendarFacade userCalendarFacade;
    private final UserProfileRepository userProfileRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final DeviceFacade deviceFacade;

    /**
     * [스케줄러]
     * 매 15분마다 실행, 해당 시간에 오후 9시(21:00)를 맞이한 타임존 중
     * 스트릭이 1 이상이며, 다가오는 자정에 48시간 유예기간이 만료되어 스트릭이 깨지는 유저에게 FCM 발송
     */
    @Scheduled(cron = "0 0,15,30,45 * * * *")
    @Transactional(readOnly = true)
    public void sendStreakReminderAt9PM() {
        Instant rawNow = Instant.now();

        int currentMinute = rawNow.atZone(ZoneOffset.UTC).getMinute();
        int targetMinute = (currentMinute / 15) * 15;
        Instant truncatedNow = rawNow.truncatedTo(ChronoUnit.HOURS)
                .plus(targetMinute, ChronoUnit.MINUTES);

        // 현재 시간이 오후 9시(21:00)인 타임존 추출
        Set<String> ninePmZones = ZoneId.getAvailableZoneIds().stream()
                .filter(zone -> {
                    LocalTime localTime = LocalTime.ofInstant(truncatedNow, ZoneId.of(zone));
                    return localTime.getHour() == 21 && localTime.getMinute() == 0;
                })
                .collect(Collectors.toSet());

        if (ninePmZones.isEmpty()) {
            return;
        }

        // 스트릭이 1 이상인 대상만 1차 조회
        List<UserProfile> targetProfiles = userProfileRepository.findTargetsForStreakReset(ninePmZones, 0);

        if (targetProfiles.isEmpty()) {
            return;
        }

        List<String> targetFcmTokens = new ArrayList<>();

        for (UserProfile profile : targetProfiles) {
            ZoneId userZone = ZoneId.of(profile.getUser().getPrimaryTimezone());
            LocalDate today = LocalDate.ofInstant(truncatedNow, userZone);
            LocalDate yesterday = today.minusDays(1); // 오늘 21시 기준 어제
            LocalDate dayBeforeYesterday = today.minusDays(2);

            WriteStatus yStatus = userCalendarFacade.getStatus(profile.getUser(), yesterday);
            WriteStatus dbyStatus = userCalendarFacade.getStatus(profile.getUser(), dayBeforeYesterday);

            // 그저께는 작성했으나(WRITTEN 또는 RECOVERED), 어제는 작성하지 않은 경우
            boolean wroteDayBefore = (dbyStatus == WriteStatus.WRITTEN || dbyStatus == WriteStatus.RECOVERED);
            boolean missedYesterday = (yStatus != WriteStatus.WRITTEN && yStatus != WriteStatus.RECOVERED);

            if (wroteDayBefore && missedYesterday) {
                List<Device> devices = deviceFacade.findAllByUserId(profile.getId());

                for (Device device : devices) {
                    String fcmToken = device.getFcmToken();
                    if (fcmToken != null && !fcmToken.isBlank()) {
                        targetFcmTokens.add(fcmToken);
                    }
                }
            }
        }

        if (targetFcmTokens.isEmpty()) {
            return;
        }

        sendMulticastPush(targetFcmTokens);
    }

    private void sendMulticastPush(List<String> tokens) {
        try {
            // MulticastMessage를 통해 여러 유저에게 동일한 알림을 한 번의 API 호출로 발송
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(Notification.builder()
                            .setTitle("일기 작성 가능 시간이 3시간 남았어요!")
                            .setBody("지금 일기를 작성하면 오늘도 불꽃을 이어갈 수 있어요🔥")
                            .build())
                    .putData("notification_type", "reminder_streak")
                    .putData("link", "hilingual://app/home")
                    .build();

            firebaseMessaging.sendEachForMulticast(message);
            log.info("{}명의 유저에게 스트릭 리마인더 푸시 발송 완료", tokens.size());
        } catch (Exception e) {
            log.error("스트릭 리마인더 푸시 발송 중 오류 발생", e);
        }
    }
}