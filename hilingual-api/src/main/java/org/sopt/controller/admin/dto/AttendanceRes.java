package org.sopt.controller.admin.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AttendanceRes(
        List<Participant> participants
) {
    public static AttendanceRes of(final List<Participant> participants) {
        return new AttendanceRes(participants);
    }

    public record Participant(
            String externalId,
            Long userId,                 // 미매칭이면 null
            List<DayAttendance> days
    ) {
        public static Participant of(final String externalId, final Long userId, final List<DayAttendance> days) {
            return new Participant(externalId, userId, days);
        }
    }

    public record DayAttendance(
            LocalDate date,
            boolean isShared,
            LocalDateTime createdAt,     // isShared=false면 null (UTC 'Z')
            Boolean isRecovered          // isShared=false면 null
    ) {
        public static DayAttendance shared(final LocalDate date, final LocalDateTime createdAt, final Boolean isRecovered) {
            return new DayAttendance(date, true, createdAt, isRecovered);
        }

        public static DayAttendance absent(final LocalDate date) {
            return new DayAttendance(date, false, null, null);
        }
    }
}