package org.sopt.noticedelivery.domain;

public class NoticeDeliveryTableConstants {
    public static final String TABLE_NOTICE_DELIVERY = "notice_delivery";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTICE_ID = "notice_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_DELIVERED_AT = "delivered_at";
    public static final String COLUMN_READ_AT = "read_at";

    // Unique Constraints
    public static final String UK_NOTICE_DELIVERY_NOTICE_USER = "uk_notice_delivery_notice_user";
}
