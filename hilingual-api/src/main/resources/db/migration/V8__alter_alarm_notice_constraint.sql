ALTER TABLE alarm_preference DROP CONSTRAINT alarm_preference_user_id_fkey;
ALTER TABLE alarm_preference ADD CONSTRAINT alarm_preference_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE notice_delivery DROP CONSTRAINT IF EXISTS notice_delivery_user_id_fkey;
ALTER TABLE notice_delivery ADD CONSTRAINT notice_delivery_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;