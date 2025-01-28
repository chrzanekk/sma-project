-- Dodanie nowych kolumn do tabeli contractors
ALTER TABLE contractors
    ADD COLUMN customer BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE contractors
    ADD COLUMN supplier BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE contractors
    ADD COLUMN scaffolding_user BOOLEAN NOT NULL DEFAULT FALSE;
