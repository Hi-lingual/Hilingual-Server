-- V11__add_recovery_chance.sql

alter table user_profile
    add column if not exists recovery_chance integer not null default 3;