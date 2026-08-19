package org.sopt.controller.widget.dto.res;

import java.time.LocalDate;

public record WidgetTopicResponse(
        LocalDate date,
        String topicEn,
        Boolean isWrittenToday
) { }