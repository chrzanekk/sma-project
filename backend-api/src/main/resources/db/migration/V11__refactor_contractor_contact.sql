ALTER TABLE contacts ADD COLUMN contractor_id BIGINT;

-- Uzupełnij contractor_id na podstawie istniejących danych
UPDATE contacts c
SET contractor_id = cc.contractor_id
FROM contractor_contacts cc
WHERE cc.contact_id = c.id;

ALTER TABLE contacts ADD CONSTRAINT fk_contacts_contractor FOREIGN KEY (contractor_id)
    REFERENCES contractors(id);

DROP TABLE contractor_contacts;
DROP SEQUENCE IF EXISTS contractor_contact_sequence;

-- === Usuwamy pakiet contractorcontact całkowicie ===