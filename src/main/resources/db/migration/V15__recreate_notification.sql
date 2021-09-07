create table NOTIFICATION
(
    ID            BIGINT auto_increment
        primary key,
    NOTIFIER      BIGINT        not null,
    RECEIVER      BIGINT        not null,
    OUTERID       BIGINT        not null,
    TYPE          INT           not null,
    GMT_CREATE    BIGINT        not null,
    STATUS        INT default 0 not null,
    NOTIFIER_NAME VARCHAR(100)  not null,
    OUTER_TITLE   VARCHAR(256)  not null
);