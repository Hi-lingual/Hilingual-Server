package org.sopt.fcm.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.device.domain.Device;
import org.sopt.device.facade.DeviceFacade;
import org.sopt.fcm.dto.TargetDeviceDto;
import org.sopt.firebase.FCMClient;
import org.sopt.firebase.dto.FCMMessageRequest;
import org.sopt.firebase.exception.FCMErrorCode;
import org.sopt.firebase.exception.FCMException;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class WinBackReminderSchedular {

    private final DeviceFacade deviceFacade;
    private final UserProfileFacade userProfileFacade;
    private final UserCalendarFacade userCalendarFacade;
    private final FCMClient fcmClient;

    /**
     * [스케줄러]
     * 매 15분마다 실행, 해당 시간에 월요일 오전 10시(10:00)를 맞이한 타임존 중
     * 1. 스트릭이 0인 유저
     * 2. 최근 7일 내 작성 완료된 일기가 존재하지 않는 유저
     * 위 조건을 만족하는 유저에게 윈백 푸시 FCM 발송
     */
    @Scheduled(cron = "0 0,15,30,45 * * * *")
    @Transactional
    public void sendWinBackReminderAtMonday10AM() {
        Instant rawNow = Instant.now();

        int currentMinute = rawNow.atZone(ZoneOffset.UTC).getMinute();
        int targetMinute = (currentMinute / 15) * 15;
        Instant truncatedNow = rawNow.truncatedTo(ChronoUnit.HOURS)
                .plus(targetMinute, ChronoUnit.MINUTES);

        // 현재 시간이 월요일이면서 오전 10시인 타임존만 추출
        Set<String> monday10AmZones = ZoneId.getAvailableZoneIds().stream()
                .filter(zone -> {
                    ZonedDateTime zdt = truncatedNow.atZone(ZoneId.of(zone));
                    return zdt.getDayOfWeek() == DayOfWeek.MONDAY &&
                            zdt.getHour() == 10 &&
                            zdt.getMinute() == 0;
                })
                .collect(Collectors.toSet());

        if (monday10AmZones.isEmpty()) {
            return;
        }

        // 스트릭이 0인 유저 조회
        // 여기서는 기존 메서드 구조에 맞춰 streak == 0 인 대상들을 가져온다고 가정합니다.
        List<UserProfile> targetProfiles = userProfileFacade.findByPrimaryTimezoneInAndStreak(monday10AmZones, 0);

        if (targetProfiles.isEmpty()) {
            return;
        }

        List<TargetDeviceDto> targetDevices = new ArrayList<>();

        for (UserProfile profile : targetProfiles) {
            ZoneId userZone = ZoneId.of(profile.getUser().getPrimaryTimezone());
            LocalDate today = LocalDate.ofInstant(truncatedNow, userZone);

            // 최근 7일간 작성된 일기가 있는지 검사
            // 오늘 기준 지난 7일 동안 WRITTEN 또는 RECOVERED 상태가 하나라도 있는지
            boolean hasRecentWrittenDiary = false;
            for (int i = 0; i < 7; i++) {
                LocalDate checkDate = today.minusDays(i);
                WriteStatus status = userCalendarFacade.getStatus(profile.getUser(), checkDate);
                if (status == WriteStatus.WRITTEN || status == WriteStatus.RECOVERED) {
                    hasRecentWrittenDiary = true;
                    break;
                }
            }

            // 최근 7일 내 작성 완료된 일기가 없는 경우에만 win back
            if (!hasRecentWrittenDiary) {
                List<Device> devices = deviceFacade.findAllByUserId(profile.getUser().getId());
                for (Device device : devices) {
                    String fcmToken = device.getFcmToken();
                    if (fcmToken != null && !fcmToken.isBlank()) {
                        targetDevices.add(new TargetDeviceDto(
                                profile.getUser().getId(),
                                device.getDeviceId(),
                                fcmToken
                        ));
                    }
                }
            }
        }

        if (targetDevices.isEmpty()) {
            return;
        }

        sendWinBackReminders(targetDevices);
    }

    private void sendWinBackReminders(List<TargetDeviceDto> targetDevices) {
        String title = "다시 시작하기 좋은 월요일이에요☀️";
        String body = "오늘의 한 문장부터 가볍게 남겨보세요.";
        Map<String, String> data = Map.of(
                "notification_type", "reminder_winback",
                "link", "hilingual://app/home"
        );

        for (TargetDeviceDto target : targetDevices) {
            try {
                FCMMessageRequest request = FCMMessageRequest.of(target.fcmToken(), title, body, data);
                fcmClient.send(request);
            } catch (FCMException e) {
                if (e.getErrorCode() == FCMErrorCode.FCM_INVALID_TOKEN) {
                    log.warn("만료되거나 유효하지 않은 FCM 토큰 감지 (윈백), 토큰 초기화 실행: userId={}, deviceId={}, token={}",
                            target.userId(), target.deviceId(), target.fcmToken());
                    deviceFacade.clearFcmToken(target.userId(), target.deviceId());
                } else {
                    log.error("윈백 푸시 발송 실패: errorCode={}, message={}", e.getErrorCode(), e.getMessage());
                }
            } catch (Exception e) {
                log.error("윈백 푸시 발송 중 예상치 못한 오류 발생", e);
            }
        }

        log.info("{}개의 기기로 윈백 푸시 발송 처리 완료", targetDevices.size());
    }
}
