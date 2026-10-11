package org.sopt.context;

import lombok.extern.slf4j.Slf4j;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
public class TimezoneContextHolder {
    private static final ThreadLocal<ZoneId> TIMEZONE_HOLDER = new ThreadLocal<>();
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Seoul");

    // 헤더에서 실제로 읽힌 유효한 값만 반환
    public static Optional<ZoneId> find() {
        return Optional.ofNullable(TIMEZONE_HOLDER.get());
    }

    // 계산 및 응답 생성용. 폴백 포함이므로 저장에 쓰지 말 것
    public static ZoneId getTimezone() {
        return find().orElse(DEFAULT_ZONE);
    }

    public static void setTimezone(String timezoneId) {
        if (timezoneId == null) {
            TIMEZONE_HOLDER.remove();
            return;
        }
        try {
            TIMEZONE_HOLDER.set(ZoneId.of(timezoneId.trim()));
        } catch (DateTimeException e) {
            log.warn("Invalid timezone header: '{}'", timezoneId);
            TIMEZONE_HOLDER.remove();
        }
    }

    public static void clear() {
        TIMEZONE_HOLDER.remove();
    }
}