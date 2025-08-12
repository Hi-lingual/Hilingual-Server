package org.sopt.alarm.domain;

public class AlarmTableConstants {
    public static final String TABLE_ALRAM_PREFERENCE = "AlarmPreference";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ALARM_TYPE = "alarm_type";
    public static final String COLUMN_IS_ENABLED = "is_enabled";

    public static final String TABLE_FEED_ALARM = "FeedAlarm";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TARGET_TYPE = "target_type";
    public static final String COLUMN_TARGET_ID = "target_id";
    public static final String COLUMN_ACTOR_ID = "actor_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_READ_AT = "read_at";

    public static final String TABLE_NOTICE_DETAIL = "NoticeDetail";
    public static final String COLUMN_NOTICE_ID = "noti_id";
    public static final String COLUMN_CONTENT = "content";

    public static final String TABLE_NOTICE = "Notice";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_IS_ACTIVEE = "is_active";
    public static final String COLUMN_NOTICE = "notice";
    public static final String COLUMN_DELIVERED_AT = "delivered_at";


    public static final String TABLE_NOTICE_DELIVERY = "NoticeDelivery";

}
