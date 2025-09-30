-- V17__add_contact_person_id_to_contracts.sql

ALTER TABLE contracts
    ADD COLUMN contact_person_id BIGINT;

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_contact_person
        FOREIGN KEY (contact_person_id) REFERENCES contacts (id);

CREATE INDEX IF NOT EXISTS idx_contracts_contact_person_id ON contracts (contact_person_id);
