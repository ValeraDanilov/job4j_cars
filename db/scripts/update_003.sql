create table cars
(
    id           serial primary key,
    body         varchar not null,
    equipment    varchar not null,
    engine       varchar not null,
    transmission varchar not null,
    driveUnit    varchar not null,
    color        varchar not null
);
