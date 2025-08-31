-- Tworzenie sekwencji
CREATE SEQUENCE contracts_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tworzenie tabeli contracts
CREATE TABLE contracts (
    id              BIGINT       NOT NULL DEFAULT nextval('contracts_sequence'),
    number          VARCHAR(150),
    description     VARCHAR(300),
    value           NUMERIC,
    start_date      TIMESTAMP WITHOUT TIME ZONE,
    end_date        TIMESTAMP WITHOUT TIME ZONE,
    signup_date     TIMESTAMP WITHOUT TIME ZONE,
    real_end_date   TIMESTAMP WITHOUT TIME ZONE,
    construction_site_id BIGINT,
    contractor_id   BIGINT,
    company_id      BIGINT       NOT NULL,
    create_date     TIMESTAMP WITHOUT TIME ZONE,
    modify_date     TIMESTAMP WITHOUT TIME ZONE,
    created_by      BIGINT,
    modified_by     BIGINT,
    CONSTRAINT contracts_pkey PRIMARY KEY (id)
);

-- Klucze obce
ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_construction_site
        FOREIGN KEY (construction_site_id) REFERENCES construction_site (id);

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_contractor
        FOREIGN KEY (contractor_id) REFERENCES contractors (id);

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_company
        FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);

-- Indeksy
CREATE INDEX IF NOT EXISTS idx_contracts_company_id ON contracts (company_id);
CREATE INDEX IF NOT EXISTS idx_contracts_contractor_id ON contracts (contractor_id);
CREATE INDEX IF NOT EXISTS idx_contracts_construction_site_id ON contracts (construction_site_id);
CREATE INDEX IF NOT EXISTS idx_contracts_created_by ON contracts (created_by);
CREATE INDEX IF NOT EXISTS idx_contracts_modified_by ON contracts (modified_by);
CREATE INDEX IF NOT EXISTS idx_contracts_number ON contracts (number);
