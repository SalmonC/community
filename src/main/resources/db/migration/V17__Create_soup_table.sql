create table soup
(
    id int,
    gmt_update int not null,
    content varchar(200) not null,
    constraint SOUP_PK
        primary key (id)
);