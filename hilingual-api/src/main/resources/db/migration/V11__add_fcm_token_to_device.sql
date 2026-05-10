ALTER TABLE device
    ADD COLUMN fcm_token VARCHAR(512),
    ADD COLUMN fcm_token_updated_at TIMESTAMPTZ;
