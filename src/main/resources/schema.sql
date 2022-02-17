create table if not exists USERS (
    id bigint identity(1, 1),
    name varchar(25) not null,
    constraint USERS_PK primary key (id)
);

create table if not exists USERS_AUTH (
    username varchar(25) not null,
    password varchar(120) not null,
    user_id bigint,
    constraint USERS_AUTH_PK primary key (username, user_id),
    constraint USERS_AUTH_FK foreign key (user_id) references USERS(id)
);
create table if not exists CHECKS (
    id bigint identity(1, 1),
    name varchar(50) not null,
    date datetime not null,
    constraint CHECKS_PK primary key (id)
);

create table if not exists PURCHASES (
    id bigint identity(1, 1),
    name varchar(50) not null,
    cost bigint not null,
    constraint PURCHASES_PK primary key (id)
);

create table if not exists USERS_CHECKS (
    user_id bigint,
    check_id bigint,
    constraint USERS_CHECKS_PK primary key (user_id, check_id),
    constraint USERS_CHECKS_USERS_FK foreign key (user_id) references USERS(id),
    constraint USERS_CHECKS_CHECKS_FK foreign key (check_id) references CHECKS(id)
);

create table if not exists CHECKS_PURCHASES (
    check_id bigint,
    purchase_id bigint,
    constraint CHECKS_PURCHASES_PK primary key (check_id, purchase_id),
    constraint CHECKS_PURCHASES_CHECKS_FK foreign key (check_id) references CHECKS(id),
    constraint CHECKS_PURCHASES_PURCHASES_FK foreign key (purchase_id) references PURCHASES(id)
);

create table if not exists PURCHASES_PAYERS (
    purchase_id bigint,
    user_id bigint,
    constraint PURCHASES_PAYERS_PK primary key (purchase_id, user_id),
    constraint PURCHASES_PAYERS_PURCHASES_FK foreign key (purchase_id) references PURCHASES(id),
    constraint PURCHASES_PAYERS_USERS_FK foreign key (user_id) references USERS(id)
);

create table if not exists PURCHASES_CONSUMERS (
    purchase_id bigint,
    user_id bigint,
    constraint PURCHASES_CONSUMERS_PK primary key (purchase_id, user_id),
    constraint PURCHASES_CONSUMERS_PURCHASES_FK foreign key (purchase_id) references PURCHASES(id),
    constraint PURCHASES_CONSUMERS_USERS_FK foreign key (user_id) references USERS(id)
);