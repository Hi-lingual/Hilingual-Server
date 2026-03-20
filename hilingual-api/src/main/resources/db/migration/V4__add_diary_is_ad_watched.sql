ALTER TABLE diary
    ADD COLUMN is_ad_watched boolean DEFAULT false;

UPDATE diary
SET is_ad_watched = false
WHERE is_ad_watched IS NULL;

ALTER TABLE diary
    ALTER COLUMN is_ad_watched SET NOT NULL;
