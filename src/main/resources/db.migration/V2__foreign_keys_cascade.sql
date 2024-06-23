alter table auth.sessions
    drop constraint sessions_users_id_fk;

alter table auth.sessions
    add constraint sessions_users_id_fk
        foreign key (user_id) references auth.users
            on delete cascade;

alter table auth.audit_log
    drop constraint audit_log_user_id_fkey;

alter table auth.audit_log
    add foreign key (user_id) references auth.users
        on delete cascade;

alter table auth.sms_code
    drop constraint sms_code_users_id_fk;

alter table auth.sms_code
    add constraint sms_code_users_id_fk
        foreign key (users_id) references auth.users
            on delete cascade;

alter table auth.user_roles
    drop constraint user_roles_users_id_fk;

alter table auth.user_roles
    add constraint user_roles_users_id_fk
        foreign key (user_id) references auth.users
            on delete cascade;

