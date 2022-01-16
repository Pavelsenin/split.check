delete from USERS;

insert into USERS (name) values ('sample_user_1');
insert into USERS (name) values ('sample_user_2');
insert into USERS (name) values ('sample_user_3');
insert into USERS (name) values ('sample_user_4');

delete from CHECKS;

insert into CHECKS (name, date) values ('sample_check_1', '2021-12-31');
insert into CHECKS (name, date) values ('sample_check_2', '2021-12-31');

delete from PURCHASES;

insert into PURCHASES (name, cost) values ('sample_purchase_1', '100');
insert into PURCHASES (name, cost) values ('sample_purchase_2', '200');
insert into PURCHASES (name, cost) values ('sample_purchase_3', '300');

delete from USERS_CHECKS;

insert into USERS_CHECKS (user_id, check_id) values ('1', '1');
insert into USERS_CHECKS (user_id, check_id) values ('2', '1');
insert into USERS_CHECKS (user_id, check_id) values ('3', '1');
insert into USERS_CHECKS (user_id, check_id) values ('1', '2');

delete from CHECKS_PURCHASES;

insert into CHECKS_PURCHASES (check_id, purchase_id) values ('1', '1');
insert into CHECKS_PURCHASES (check_id, purchase_id) values ('1', '2');
insert into CHECKS_PURCHASES (check_id, purchase_id) values ('1', '3');

delete from PURCHASES_PAYERS;

insert into PURCHASES_PAYERS (purchase_id, user_id) values ('1', '2');
insert into PURCHASES_PAYERS (purchase_id, user_id) values ('2', '2');
insert into PURCHASES_PAYERS (purchase_id, user_id) values ('3', '3');

delete from PURCHASES_CONSUMERS;

insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('1', '1');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('1', '2');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('1', '3');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('2', '1');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('2', '2');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('2', '3');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('3', '1');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('3', '2');
insert into PURCHASES_CONSUMERS (purchase_id, user_id) values ('3', '3');