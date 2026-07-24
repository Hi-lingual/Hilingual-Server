-- V18__add_external_id_to_users.sql

create extension if not exists pgcrypto;

alter table users
    add column if not exists external_id varchar(64);

-- 기존 유저 백필: provider + ':' + provider_id 를 SHA-256 hex 로
update users
set external_id = encode(digest(provider || ':' || provider_id, 'sha256'), 'hex')
where external_id is null;

alter table users
    alter column external_id set not null;

create unique index if not exists ux_users_external_id on users (external_id);