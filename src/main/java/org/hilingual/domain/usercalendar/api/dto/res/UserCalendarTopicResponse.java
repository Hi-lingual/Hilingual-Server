package org.hilingual.domain.usercalendar.api.dto.res;

public record UserCalendarTopicResponse(
        String topicKor,
        String topicEn,
        int remainingTime
) {
    public static UserCalendarTopicResponse of(String topicKor, String topicEn, int remainingTime) {
        return new UserCalendarTopicResponse(topicKor, topicEn, remainingTime);
    }
}
