-- V13__add_is_recovered_to_diary.sql

alter table diary
    add column is_recovered boolean not null default false;