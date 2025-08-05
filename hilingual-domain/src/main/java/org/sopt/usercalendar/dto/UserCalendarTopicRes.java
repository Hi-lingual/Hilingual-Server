package org.sopt.usercalendar.dto;

public record UserCalendarTopicRes(
        String topicKor,
        String topicEn,
        int remainingTime
) {
    public static UserCalendarTopicRes of(String topicKor, String topicEn, int remainingTime) {
        return new UserCalendarTopicRes(topicKor, topicEn, remainingTime);
    }
}
