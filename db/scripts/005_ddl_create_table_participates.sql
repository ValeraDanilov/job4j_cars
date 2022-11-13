create table  if not exists participates
(
    id      serial primary key,
    post_id int not null references post (id),
    user_id int not null references auto_user (id)
);
