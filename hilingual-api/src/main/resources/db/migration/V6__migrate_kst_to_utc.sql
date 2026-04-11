-- shared_time만 9시간 당기기(KST -> UTC)
UPDATE diary
SET shared_time = shared_time - INTERVAL '9 hours'
WHERE shared_time IS NOT NULL;

-- BaseTimeEntity 컬럼들도 9시간 당김
UPDATE users SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE diary SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE block SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE device SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE diary_feedback SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE feed_alarm SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE follow SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE liked_diary SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE notice SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE recommend SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE user_calendar SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE user_profile SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
UPDATE voca SET created_at = created_at - INTERVAL '9 hours', updated_at = updated_at - INTERVAL '9 hours';
