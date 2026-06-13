-- V12__add_recovery_chance_count.sql

alter table user_profile
    add column recovery_chance_count integer not null default 3;