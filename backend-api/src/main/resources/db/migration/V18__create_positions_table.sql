-- Tworzenie sekwencji
CREATE SEQUENCE positions_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Tworzenie tabeli positions
CREATE TABLE positions (
                           id              BIGINT       NOT NULL DEFAULT nextval('positions_sequence'),
                           name            VARCHAR(255) NOT NULL,
                           description     VARCHAR(500),
                           company_id      BIGINT       NOT NULL,
                           create_date     TIMESTAMP WITHOUT TIME ZONE,
                           modify_date     TIMESTAMP WITHOUT TIME ZONE,
                           created_by      BIGINT,
                           modified_by     BIGINT,
                           CONSTRAINT positions_pkey PRIMARY KEY (id)
);

-- Klucze obce
ALTER TABLE positions
    ADD CONSTRAINT fk_positions_company
        FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE positions
    ADD CONSTRAINT fk_positions_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE positions
    ADD CONSTRAINT fk_positions_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);

-- Indeksy
CREATE INDEX IF NOT EXISTS idx_positions_company_id ON positions (company_id);
CREATE INDEX IF NOT EXISTS idx_positions_created_by ON positions (created_by);
CREATE INDEX IF NOT EXISTS idx_positions_modified_by ON positions (modified_by);
CREATE INDEX IF NOT EXISTS idx_positions_name ON positions (name);
