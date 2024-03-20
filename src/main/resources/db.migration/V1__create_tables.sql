create schema auth;

create table auth.users
(
    id                bigserial
        primary key,
    email             varchar(255)            not null,
    password_hash     varchar(100)            not null,
    registration_at   timestamp default now(),
    phone_number      varchar(9)              not null,
    is_block          boolean   default false not null,
    password_reset_at timestamp
);

create unique index users_email_uindex
    on auth.users (email);

create unique index users_phone_number_uindex
    on auth.users (phone_number);

create table auth.roles
(
    id   bigserial
        primary key,
    name varchar(16) not null
);

create unique index roles_name_uindex
    on auth.roles (name);

create table auth.user_roles
(
    user_id bigint not null
        constraint user_roles_users_id_fk
            references auth.users,
    role_id bigint not null
        constraint user_roles_roles_id_fk
            references auth.roles,
    constraint user_roles_pk
        primary key (user_id, role_id)
);

create table auth.log_actions
(
    id          bigserial
        primary key,
    name        varchar(32),
    description varchar(255)
);

create table auth.audit_log
(
    id        bigserial
        primary key,
    user_id   bigint not null
        references auth.users,
    at        timestamp default now(),
    ip_v4     inet,
    action_id bigint not null
        constraint audit_log_log_actions_id_fk
            references auth.log_actions
);

create table auth.sessions
(
    id         bigserial
        constraint sessions_pk
            primary key,
    user_id    bigint                  not null
        constraint sessions_users_id_fk
            references auth.users,
    start_at   timestamp default now() not null,
    ip_v4      inet,
    user_agent text
);

create table auth.sms_code
(
    users_id bigint                  not null
        constraint sms_code_pk
            primary key
        constraint sms_code_users_id_fk
            references auth.users,
    code     varchar(4)              not null,
    at       timestamp default now() not null
);

