ALTER TABLE liked_diary DROP CONSTRAINT liked_diary_diary_id_fkey;

ALTER TABLE liked_diary
    ADD CONSTRAINT liked_diary_diary_id_fkey
    FOREIGN KEY (diary_id)
    REFERENCES diary(id)
    ON DELETE CASCADE;

ALTER TABLE liked_diary DROP CONSTRAINT liked_diary_user_id_fkey;

ALTER TABLE liked_diary
    ADD CONSTRAINT liked_diary_user_id_fkey
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE;