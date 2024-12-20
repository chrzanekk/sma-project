CREATE TABLE contractor
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(200)                            NOT NULL,
    tax_number   VARCHAR(255),
    street       VARCHAR(255),
    building_no  VARCHAR(255),
    apartment_no VARCHAR(255),
    postal_code  VARCHAR(255),
    city         VARCHAR(255),
    country      VARCHAR(255),
    create_date  TIMESTAMP WITHOUT TIME ZONE,
    modify_date  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_contractor PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(20),
    create_date TIMESTAMP WITHOUT TIME ZONE,
    modify_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE send_emails
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title       VARCHAR(255),
    content     VARCHAR(5000),
    event       VARCHAR(255),
    language    VARCHAR(255),
    user_id     BIGINT                                  NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_send_emails PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);

CREATE TABLE user_tokens
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token         VARCHAR(255),
    user_id       BIGINT                                  NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE,
    expire_date   TIMESTAMP WITHOUT TIME ZONE,
    use_date      TIMESTAMP WITHOUT TIME ZONE,
    token_type    VARCHAR(255),
    CONSTRAINT pk_user_tokens PRIMARY KEY (id)
);

CREATE TABLE users
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email       VARCHAR(50)                             NOT NULL,
    username    VARCHAR(20)                             NOT NULL,
    password    VARCHAR(120),
    locked      BOOLEAN,
    enabled     BOOLEAN,
    create_date TIMESTAMP WITHOUT TIME ZONE,
    modify_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_77584fbe74cc86922be2a3560 UNIQUE (username);

ALTER TABLE send_emails
    ADD CONSTRAINT FK_SEND_EMAILS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_tokens
    ADD CONSTRAINT FK_USER_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);