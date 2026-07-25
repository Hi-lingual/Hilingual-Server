package org.sopt.controller.admin.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record AttendanceReq(
        @NotEmpty
        @Size(max = 100, message = "externalIds는 최대 100개까지 조회할 수 있습니다.")
        List<String> externalIds,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate
) {
    @AssertTrue(message = "startDate는 endDate보다 이후일 수 없습니다.")
    public boolean isValidRange() {
        if (startDate == null || endDate == null) {
            return true; // null은 @NotNull이 처리
        }
        return !startDate.isAfter(endDate);
    }
}