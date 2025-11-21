-- V25__create_units_table.sql
-- Description: Create units table with base measurement units

-- =============================================================================
-- SEQUENCE
-- =============================================================================
CREATE SEQUENCE unit_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- =============================================================================
-- UNITS TABLE
-- =============================================================================
CREATE TABLE units (
                       id BIGINT PRIMARY KEY DEFAULT nextval('unit_sequence'),
                       symbol VARCHAR(16) NOT NULL,
                       description VARCHAR(128),
                       unit_type VARCHAR(32) NOT NULL,
                       company_id BIGINT NOT NULL,
                       create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       created_by BIGINT,
                       modify_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       modified_by BIGINT,

                       CONSTRAINT fk_unit_company FOREIGN KEY(company_id)
                           REFERENCES company(id) ON DELETE RESTRICT,
                       CONSTRAINT fk_unit_created_by FOREIGN KEY(created_by)
                           REFERENCES users(id),
                       CONSTRAINT fk_unit_modified_by FOREIGN KEY(modified_by)
                           REFERENCES users(id)
);

-- Indeksy dla wydajności
CREATE INDEX idx_units_company ON units(company_id);
CREATE INDEX idx_units_symbol_company ON units(symbol, company_id);
CREATE INDEX idx_units_type ON units(unit_type);

-- Unique constraint - symbol + company
CREATE UNIQUE INDEX uk_unit_symbol_company ON units(symbol, company_id);

-- =============================================================================
-- BAZOWE JEDNOSTKI (dla company_id = 1, można powtórzyć dla innych firm)
-- =============================================================================
INSERT INTO units (symbol, description, unit_type, company_id)
VALUES
    ('m2', 'Metr kwadratowy', 'AREA', 1),
    ('m3', 'Metr sześcienny', 'VOLUME', 1),
    ('mb', 'Metr bieżący', 'LENGTH', 1),
    ('r-h', 'Roboczogodzina', 'DURATION', 1),
    ('km', 'Kilometr', 'LENGTH', 1),
    ('k/h', 'Kilometry na godzinę', 'SPEED', 1),
    ('m2/doba', 'Metr kwadratowy na dobę', 'DENSITY', 1),
    ('m3/doba', 'Metr sześcienny na dobę', 'DENSITY', 1),
    ('mb/doba', 'Metr bieżący na dobę', 'DENSITY', 1),
    ('kN/m', 'Kiloniutony na metr', 'FORCE', 1);

-- =============================================================================
-- KOMENTARZE
-- =============================================================================
COMMENT ON TABLE units IS 'Measurement units used throughout the system (area, volume, length, etc.)';
COMMENT ON COLUMN units.symbol IS 'Short unit symbol, e.g. m2, m3, mb, r-h';
COMMENT ON COLUMN units.description IS 'Full unit name in Polish';
COMMENT ON COLUMN units.unit_type IS 'Type of unit: LENGTH, AREA, VOLUME, DURATION, SPEED, DENSITY, FORCE';
COMMENT ON COLUMN units.company_id IS 'Company owning this unit definition';
