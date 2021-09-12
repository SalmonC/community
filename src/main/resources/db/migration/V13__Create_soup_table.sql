create table soup
(
    id int,
    gmt_update bigint not null,
    content varchar(200) not null,
    constraint soup_pk
        primary key (id)
);