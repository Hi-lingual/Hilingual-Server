ALTER TABLE users
    ADD COLUMN primary_timezone VARCHAR(255) DEFAULT 'Asia/Seoul';

UPDATE users
SET primary_timezone = 'Asia/Seoul'
WHERE primary_timezone IS NULL;

ALTER TABLE users
    ALTER COLUMN primary_timezone SET NOT NULL;