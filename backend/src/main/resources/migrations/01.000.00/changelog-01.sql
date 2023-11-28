--liquibase formatted sql

--changeset volovnik-ai:1-0

create table if not exists documents
(
    id           serial primary key,
    type         varchar(255) not null,
    description  varchar(255) not null,
    organization varchar(255) not null,
    patient      varchar(255) not null,
    date         timestamp    not null,
    status       varchar(255) not null
);

create table if not exists outbox
(
    id      uuid primary key,
    topic   varchar(512)       not null,
    payload varchar(2000)      not null,
    sent    bool default false not null
);

create table if not exists inbox
(
    id        uuid primary key,
    topic     varchar(512)       not null,
    payload   varchar(2000)      not null,
    completed bool default false not null
);
