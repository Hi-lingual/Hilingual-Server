-- V16__update_recovery_ticket_cascade.sql

ALTER TABLE recovery_ticket
    DROP CONSTRAINT IF EXISTS recovery_ticket_user_id_fkey;

-- 2. ON DELETE CASCADE가 포함된 새로운 외래 키 제약조건을 추가합니다.
ALTER TABLE recovery_ticket
    ADD CONSTRAINT recovery_ticket_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;