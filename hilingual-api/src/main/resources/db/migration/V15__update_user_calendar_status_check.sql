-- V15__update_user_calendar_status_check

ALTER TABLE user_calendar
    DROP CONSTRAINT IF EXISTS user_calendar_status_check;

ALTER TABLE user_calendar
    ADD CONSTRAINT user_calendar_status_check
        CHECK ((status)::text = ANY (ARRAY['NONE'::varchar, 'WRITTEN'::varchar, 'DELETED'::varchar, 'RECOVERED'::varchar]::text[]));