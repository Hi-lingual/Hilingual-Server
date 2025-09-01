package org.sopt.user.type;

public enum NotificationTab {
    FEED,
    NOTIFICATION
    ;

    public static NotificationTab from(String raw) {
        try {
            return NotificationTab.valueOf(raw.toUpperCase());
        } catch (Exception e) {
            return FEED;
        }
    }
}