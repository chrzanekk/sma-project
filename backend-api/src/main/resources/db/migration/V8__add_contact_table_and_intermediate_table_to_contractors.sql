ALTER TABLE user_roles
    RENAME CONSTRAINT fk_userol_on_role TO fk_userrole_on_role;

ALTER TABLE user_roles
    RENAME CONSTRAINT fk_userol_on_user TO fk_userrole_on_user;

-- Utworzenie sekwencji dla tabeli contacts
CREATE SEQUENCE contacts_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Utworzenie tabeli contacts
CREATE TABLE contacts (
                          id BIGINT NOT NULL DEFAULT nextval('contacts_sequence'),
                          first_name VARCHAR(100),
                          last_name VARCHAR(100),
                          phone_number VARCHAR(15),
                          email VARCHAR(50),
                          additional_info VARCHAR(250),
                          create_date TIMESTAMP WITHOUT TIME ZONE,
                          modify_date TIMESTAMP WITHOUT TIME ZONE,
                          CONSTRAINT contacts_pkey PRIMARY KEY (id)
);

-- Utworzenie sekwencji dla tabeli contractor_contacts
CREATE SEQUENCE contractor_contacts_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Utworzenie tabeli contractor_contacts
CREATE TABLE contractor_contacts (
                                     id BIGINT NOT NULL DEFAULT nextval('contractor_contacts_sequence'),
                                     contractor_id BIGINT NOT NULL,
                                     contact_id BIGINT NOT NULL,
                                     CONSTRAINT contractor_contacts_pkey PRIMARY KEY (id),
                                     CONSTRAINT fk_contractor_contacts_contractor FOREIGN KEY (contractor_id) REFERENCES contractors (id),
                                     CONSTRAINT fk_contractor_contacts_contact FOREIGN KEY (contact_id) REFERENCES contacts (id)
);