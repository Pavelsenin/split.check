delete from users;

insert into users (name) values ('sample_user_1');
insert into users (name) values ('sample_user_2');
insert into users (name) values ('sample_user_3');
insert into users (name) values ('sample_user_4');

delete from users_auth;

insert into users_auth (username, password, user_id) values ('sample_username_1', '111', 1);
insert into users_auth (username, password, user_id) values ('sample_username_2', '222', 2);
insert into users_auth (username, password, user_id) values ('sample_username_3', '333', 3);
insert into users_auth (username, password, user_id) values ('sample_username_4', '444', 4);

delete from friends_requests;

delete from checks;

insert into checks (name, date) values ('sample_check_1', '2021-12-31');
insert into checks (name, date) values ('sample_check_2', '2021-12-31');

delete from purchases;

insert into purchases (name, cost) values ('sample_purchase_1', '100');
insert into purchases (name, cost) values ('sample_purchase_2', '200');
insert into purchases (name, cost) values ('sample_purchase_3', '300');

delete from users_checks;

insert into users_checks (user_id, check_id) values ('1', '1');
insert into users_checks (user_id, check_id) values ('2', '1');
insert into users_checks (user_id, check_id) values ('3', '1');
insert into users_checks (user_id, check_id) values ('1', '2');

delete from checks_purchases;

insert into checks_purchases (check_id, purchase_id) values ('1', '1');
insert into checks_purchases (check_id, purchase_id) values ('1', '2');
insert into checks_purchases (check_id, purchase_id) values ('1', '3');

delete from purchases_payers;

insert into purchases_payers (purchase_id, user_id) values ('1', '2');
insert into purchases_payers (purchase_id, user_id) values ('2', '2');
insert into purchases_payers (purchase_id, user_id) values ('3', '3');

delete from purchases_consumers;

insert into purchases_consumers (purchase_id, user_id) values ('1', '1');
insert into purchases_consumers (purchase_id, user_id) values ('1', '2');
insert into purchases_consumers (purchase_id, user_id) values ('1', '3');
insert into purchases_consumers (purchase_id, user_id) values ('2', '1');
insert into purchases_consumers (purchase_id, user_id) values ('2', '2');
insert into purchases_consumers (purchase_id, user_id) values ('2', '3');
insert into purchases_consumers (purchase_id, user_id) values ('3', '1');
insert into purchases_consumers (purchase_id, user_id) values ('3', '2');
insert into purchases_consumers (purchase_id, user_id) values ('3', '3');