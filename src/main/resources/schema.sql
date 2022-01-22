create table if not exists USERS (
    id bigint identity(1, 1),
    name varchar(25) not null
);

create table if not exists CHECKS (
    id bigint identity(1, 1),
    name varchar(50) not null,
    date datetime not null
);

create table if not exists PURCHASES (
    id bigint identity(1, 1),
    name varchar(50) not null,
    cost bigint not null
);


create table if not exists USERS_CHECKS (
    user_id bigint,
    check_id bigint
);
alter table USERS_CHECKS add foreign key (user_id) references USERS(id);
alter table USERS_CHECKS add foreign key (check_id) references CHECKS(id);

create table if not exists CHECKS_PURCHASES (
    check_id bigint,
    purchase_id bigint
);
alter table CHECKS_PURCHASES add foreign key (check_id) references CHECKS(id);
alter table CHECKS_PURCHASES add foreign key (purchase_id) references PURCHASES(id);

create table if not exists PURCHASES_PAYERS (
    purchase_id bigint,
    user_id bigint
);
alter table PURCHASES_PAYERS add foreign key (purchase_id) references PURCHASES(id);
alter table PURCHASES_PAYERS add foreign key (user_id) references USERS(id);

create table if not exists PURCHASES_CONSUMERS (
    purchase_id bigint,
    user_id bigint
);
alter table PURCHASES_CONSUMERS add foreign key (purchase_id) references PURCHASES(id);
alter table PURCHASES_CONSUMERS add foreign key (user_id) references USERS(id);
