package org.sopt.controller.widget.dto.res;

import java.time.LocalDate;
import java.util.List;

public record WidgetStreakResponse(
        int streak,
        List<RecentDay> recentDays
) {
    public record RecentDay(
            LocalDate date,
            String dayOfWeek,
            boolean isWritten
    ) {}
}