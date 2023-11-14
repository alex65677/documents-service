--liquibase formatted sql

--changeset volovnik-ai:1-0

create table documents (
    id serial primary key,
    type varchar(255) not null,
    description varchar(255) not null,
    organization varchar(255) not null,
    patient varchar(255) not null,
    date timestamp not null,
    status varchar(255) not null
);
