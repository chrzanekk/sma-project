-- V28__update_scaffolding_dimensions.sql
-- Description: Rename full_dimension to partial, add new full_dimension and partial dimension unit

-- =============================================================================
-- 1. ZMIANA NAZWY ISTNIEJĄCEGO WYMIARU (FULL -> PARTIAL)
-- =============================================================================
ALTER TABLE scaffolding_log_position
    RENAME COLUMN scaffolding_full_dimension TO scaffolding_partial_dimension;

-- =============================================================================
-- 2. DODANIE NOWEJ KOLUMNY DLA FULL DIMENSION
-- =============================================================================
ALTER TABLE scaffolding_log_position
    ADD COLUMN scaffolding_full_dimension NUMERIC(19,4) DEFAULT 0;

-- Opcjonalne wyrównanie wartości NULL
UPDATE scaffolding_log_position
SET scaffolding_full_dimension = 0
WHERE scaffolding_full_dimension IS NULL;

-- =============================================================================
-- 3. DODANIE JEDNOSTKI DLA PARTIAL DIMENSION
-- =============================================================================
ALTER TABLE scaffolding_log_position
    ADD COLUMN scaffolding_partial_dimension_unit_id BIGINT;

-- Zaktualizowanie nowej kolumny na podstawie dotychczasowej jednostki (z migracji V26)
-- Skoro stary 'full' stał się 'partial', używamy tej samej jednostki.
UPDATE scaffolding_log_position
SET scaffolding_partial_dimension_unit_id = scaffolding_full_dimension_unit_id
WHERE scaffolding_partial_dimension_unit_id IS NULL;

-- Sprawdzenie poprawności przed nałożeniem NOT NULL
DO $$
DECLARE
null_count INTEGER;
BEGIN
SELECT COUNT(*) INTO null_count
FROM scaffolding_log_position
WHERE scaffolding_partial_dimension_unit_id IS NULL;

IF null_count > 0 THEN
        RAISE EXCEPTION 'Found % records without partial_dimension_unit_id!', null_count;
END IF;
END $$;

-- Dodanie klucza obcego, indeksu i wymuszenie NOT NULL
ALTER TABLE scaffolding_log_position
    ADD CONSTRAINT fk_position_partial_dimension_unit
        FOREIGN KEY (scaffolding_partial_dimension_unit_id) REFERENCES units (id) ON DELETE RESTRICT;

CREATE INDEX idx_position_partial_dimension_unit ON scaffolding_log_position (scaffolding_partial_dimension_unit_id);

ALTER TABLE scaffolding_log_position
    ALTER COLUMN scaffolding_partial_dimension_unit_id SET NOT NULL;

-- =============================================================================
-- 4. AKTUALIZACJA KOMENTARZY
-- =============================================================================
COMMENT ON COLUMN scaffolding_log_position.scaffolding_partial_dimension IS 'Partial dimension of scaffolding with 4 decimal places precision (previously full_dimension)';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_full_dimension IS 'Total aggregated dimension of scaffolding with 4 decimal places precision';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_partial_dimension_unit_id IS 'Measurement unit for partial scaffolding dimension';