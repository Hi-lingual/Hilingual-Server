package org.sopt.context;

import java.time.ZoneId;

public class TimezoneContextHolder {
    private static final ThreadLocal<ZoneId> TIMEZONE_HOLDER = new ThreadLocal<>();

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Seoul");

    public static void setTimezone(String timezoneId) {
        try {
            TIMEZONE_HOLDER.set(ZoneId.of(timezoneId));
        } catch (Exception e) {
            TIMEZONE_HOLDER.set(DEFAULT_ZONE); // 잘못된 값이 오면 기본값 사용
        }
    }

    public static ZoneId getTimezone() {
        return TIMEZONE_HOLDER.get() != null ? TIMEZONE_HOLDER.get() : DEFAULT_ZONE;
    }

    public static void clear() {
        TIMEZONE_HOLDER.remove();
    }
}