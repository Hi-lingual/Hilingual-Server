-- V12__add_is_recovered_to_diary.sql

alter table diary
    add column if not exists is_recovered boolean not null default false;