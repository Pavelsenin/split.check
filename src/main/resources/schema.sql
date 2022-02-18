CREATE TABLE IF NOT EXISTS users (
    id BIGINT IDENTITY(1, 1),
    name VARCHAR(25) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_auth (
    username VARCHAR(25) NOT NULL,
    password VARCHAR(60) NOT NULL,
    user_id BIGINT,
    CONSTRAINT users_auth_pk PRIMARY KEY (username, user_id),
    CONSTRAINT users_auth_fk FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS checks (
    id BIGINT IDENTITY(1, 1),
    name VARCHAR(50) NOT NULL,
    DATE datetime NOT NULL,
    CONSTRAINT checks_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS purchases (
    id BIGINT IDENTITY(1, 1),
    name VARCHAR(50) NOT NULL,
    cost BIGINT NOT NULL,
    CONSTRAINT purchases_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_checks (
    user_id BIGINT,
    check_id BIGINT,
    CONSTRAINT users_checks_PK PRIMARY KEY (user_id, check_id),
    CONSTRAINT users_checks_users_fk FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT users_checks_checks_fk FOREIGN KEY (check_id) REFERENCES checks(id)
);

CREATE TABLE IF NOT EXISTS checks_purchases (
    check_id BIGINT,
    purchase_id BIGINT,
    CONSTRAINT checks_purchases_pk PRIMARY KEY (check_id, purchase_id),
    CONSTRAINT checks_purchases_checks_fk FOREIGN KEY (check_id) REFERENCES checks(id),
    CONSTRAINT checks_purchases_purchases_fk FOREIGN KEY (purchase_id) REFERENCES purchases(id)
);

CREATE TABLE IF NOT EXISTS purchases_payers (
    purchase_id BIGINT,
    user_id BIGINT,
    CONSTRAINT purchases_payers_pk PRIMARY KEY (purchase_id, user_id),
    CONSTRAINT purchases_payers_purchases_fk FOREIGN KEY (purchase_id) REFERENCES purchases(id),
    CONSTRAINT purchases_payers_users_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS purchases_consumers (
    purchase_id BIGINT,
    user_id BIGINT,
    CONSTRAINT purchases_consumers_pk PRIMARY KEY (purchase_id, user_id),
    CONSTRAINT purchases_consumers_purchases_fk FOREIGN KEY (purchase_id) REFERENCES purchases(id),
    CONSTRAINT purchases_consumers_users_fk FOREIGN KEY (user_id) REFERENCES users(id)
);