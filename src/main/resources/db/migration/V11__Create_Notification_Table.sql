create table notification
(
    id         bigint auto_increment PRIMARY KEY,
    notifier   bigint        not null,
    receiver   bigint        not null,
    "outerId"  bigint        not null,
    type       int           not null,
    gmt_create bigint        not null,
    status     int default 0 not null,
);
