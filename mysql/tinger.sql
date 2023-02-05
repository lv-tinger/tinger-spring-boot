create table config
(
    id      varchar(36) not null
        primary key,
    `key`   varchar(36) null,
    title   varchar(36) null,
    content text        null
);

create table member
(
    id    bigint not null
        primary key,
    level int    null,
    st    int    null,
    et    bigint null,
    ct    bigint null,
    ut    bigint null
);

create table user
(
    id bigint not null
        primary key,
    st int    null,
    ct bigint null,
    ut bigint null
);

create table version
(
    id      varchar(36) not null
        primary key,
    `key`   varchar(36) null,
    version int         null
);

