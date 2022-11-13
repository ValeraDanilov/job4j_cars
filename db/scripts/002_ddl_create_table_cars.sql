create table if not exists cars
(
    id           serial primary key,
    body         varchar not null,
    equipment    varchar not null,
    transmission varchar not null,
    driveUnit    varchar not null,
    color        varchar not null,
    engine_id int not null references engine(id)
    );
