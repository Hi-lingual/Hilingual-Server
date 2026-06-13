ALTER TABLE user_profile
    ADD COLUMN recovery_chance INTEGER NOT NULL DEFAULT 3;