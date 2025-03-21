-- V10__create_company_update_contacts_contractors.sql

-----------------------------------------------------------
-- Utworzenie sekwencji dla tabeli company
-----------------------------------------------------------
CREATE SEQUENCE company_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-----------------------------------------------------------
-- Utworzenie tabeli company z polami audytu oraz odniesieniami do użytkowników
-----------------------------------------------------------
CREATE TABLE company (
                         id BIGINT NOT NULL DEFAULT nextval('company_sequence'),
                         name VARCHAR(200) NOT NULL UNIQUE,
                         additional_info VARCHAR(300),
                         create_date TIMESTAMP WITHOUT TIME ZONE,
                         modify_date TIMESTAMP WITHOUT TIME ZONE,
                         created_by BIGINT,
                         modified_by BIGINT,
                         CONSTRAINT company_pkey PRIMARY KEY (id),
                         CONSTRAINT fk_company_created_by FOREIGN KEY (created_by) REFERENCES users (id),
                         CONSTRAINT fk_company_modified_by FOREIGN KEY (modified_by) REFERENCES users (id)
);

-----------------------------------------------------------
-- Utworzenie sekwencji dla tabeli user_companies (tabela pośrednia)
-----------------------------------------------------------
CREATE SEQUENCE user_companies_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-----------------------------------------------------------
-- Utworzenie tabeli user_companies
-----------------------------------------------------------
CREATE TABLE user_companies (
                                id BIGINT NOT NULL DEFAULT nextval('user_companies_sequence'),
                                user_id BIGINT NOT NULL,
                                company_id BIGINT NOT NULL,
                                CONSTRAINT user_companies_pkey PRIMARY KEY (id),
                                CONSTRAINT fk_user_companies_user FOREIGN KEY (user_id) REFERENCES users (id),
                                CONSTRAINT fk_user_companies_company FOREIGN KEY (company_id) REFERENCES company (id)
);

-----------------------------------------------------------
-- Aktualizacja tabeli contacts - dodanie kolumny company_id oraz klucza obcego
-----------------------------------------------------------
ALTER TABLE contacts ADD COLUMN company_id BIGINT;
ALTER TABLE contacts
    ADD CONSTRAINT fk_contacts_company FOREIGN KEY (company_id) REFERENCES company (id);

-----------------------------------------------------------
-- Aktualizacja tabeli contractors - dodanie kolumny company_id oraz klucza obcego
-----------------------------------------------------------
ALTER TABLE contractors ADD COLUMN company_id BIGINT;
ALTER TABLE contractors
    ADD CONSTRAINT fk_contractors_company FOREIGN KEY (company_id) REFERENCES company (id);
