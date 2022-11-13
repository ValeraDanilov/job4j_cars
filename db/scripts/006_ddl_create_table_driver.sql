create table if not exists driver
(
    id serial primary key,
    name varchar,
    user_id int not null references auto_user(id)
);
