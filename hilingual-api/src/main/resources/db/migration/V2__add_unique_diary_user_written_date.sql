-- V2__add_diary_unique_user_written_date.sql

ALTER TABLE diary
    ADD CONSTRAINT uk_diary_user_written_date
        UNIQUE (user_id, written_date);
