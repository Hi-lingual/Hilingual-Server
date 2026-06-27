-- V17__add_written_date_to_voca

ALTER TABLE voca
    ADD COLUMN IF NOT EXISTS written_date DATE;

UPDATE voca v
SET written_date = d.written_date
FROM recommend r
JOIN diary d ON d.id = r.diary_id
WHERE v.recommend_id = r.id AND v.saved_root = 1;