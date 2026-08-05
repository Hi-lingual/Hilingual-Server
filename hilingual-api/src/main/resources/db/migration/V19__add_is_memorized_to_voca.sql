-- V19__add_is_memorized_to_voca.sql

ALTER TABLE voca
    ADD COLUMN is_memorized BOOLEAN NOT NULL DEFAULT FALSE;