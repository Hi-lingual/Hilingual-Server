--- V10__add_cascade_all_user_relations.sql

ALTER TABLE feed_alarm DROP CONSTRAINT IF EXISTS feed_alarm_user_id_fkey;
ALTER TABLE feed_alarm ADD CONSTRAINT feed_alarm_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE user_calendar DROP CONSTRAINT IF EXISTS user_calendar_user_id_fkey;
ALTER TABLE user_calendar
    ADD CONSTRAINT user_calendar_user_id_fkey
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

ALTER TABLE voca DROP CONSTRAINT IF EXISTS voca_user_id_fkey;
ALTER TABLE voca
    ADD CONSTRAINT voca_user_id_fkey
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;

ALTER TABLE device DROP CONSTRAINT IF EXISTS fk_device_user;
ALTER TABLE device
    ADD CONSTRAINT fk_device_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE;