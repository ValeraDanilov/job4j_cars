create table if not exists auto_user
(
    id       serial primary key,
    login    varchar not null,
    password varchar not null
);

create table if not exists post
(
    id           serial primary key,
    text         text not null,
    created      timestamp,
    photo        bytea,
    auto_user_id int references auto_user (id),
    car_id       int  not null references cars (id)
);
