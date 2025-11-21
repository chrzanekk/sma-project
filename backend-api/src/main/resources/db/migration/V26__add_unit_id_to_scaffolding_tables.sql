-- V26__add_unit_id_to_scaffolding_tables.sql
-- Description: Add unit_id foreign key to scaffolding_log_position, scaffolding_log_position_dimensions and scaffolding_log_position_working_time

-- =============================================================================
-- ADD UNIT_ID TO SCAFFOLDING_LOG_POSITION (dla scaffoldingFullDimension)
-- =============================================================================
ALTER TABLE scaffolding_log_position
    ADD COLUMN scaffolding_full_dimension_unit_id BIGINT;

ALTER TABLE scaffolding_log_position
    ADD CONSTRAINT fk_position_full_dimension_unit
        FOREIGN KEY(scaffolding_full_dimension_unit_id) REFERENCES units(id) ON DELETE RESTRICT;

CREATE INDEX idx_position_full_dimension_unit ON scaffolding_log_position(scaffolding_full_dimension_unit_id);

-- Ustaw domyślną jednostkę m2 dla istniejących rekordów (jeśli są dane)
-- Zakładamy że większość rusztowań to powierzchnia (m2)
UPDATE scaffolding_log_position
SET scaffolding_full_dimension_unit_id = (
    SELECT id FROM units
    WHERE symbol = 'm2' AND company_id = scaffolding_log_position.company_id
    LIMIT 1
    )
WHERE scaffolding_full_dimension_unit_id IS NULL;

-- Teraz możemy dodać NOT NULL constraint
ALTER TABLE scaffolding_log_position
    ALTER COLUMN scaffolding_full_dimension_unit_id SET NOT NULL;

-- =============================================================================
-- ADD UNIT_ID TO SCAFFOLDING_LOG_POSITION_DIMENSIONS
-- =============================================================================
ALTER TABLE scaffolding_log_position_dimensions
    ADD COLUMN unit_id BIGINT;

ALTER TABLE scaffolding_log_position_dimensions
    ADD CONSTRAINT fk_dimension_unit
        FOREIGN KEY(unit_id) REFERENCES units(id) ON DELETE RESTRICT;

CREATE INDEX idx_dimension_unit ON scaffolding_log_position_dimensions(unit_id);

-- Ustaw domyślną jednostkę m2 dla istniejących rekordów (jeśli istnieją)
UPDATE scaffolding_log_position_dimensions
SET unit_id = (
    SELECT id FROM units
    WHERE symbol = 'm2' AND company_id = scaffolding_log_position_dimensions.company_id
    LIMIT 1
    )
WHERE unit_id IS NULL;

-- Teraz możemy dodać NOT NULL constraint
ALTER TABLE scaffolding_log_position_dimensions
    ALTER COLUMN unit_id SET NOT NULL;

-- =============================================================================
-- ADD UNIT_ID TO SCAFFOLDING_LOG_POSITION_WORKING_TIME
-- =============================================================================
ALTER TABLE scaffolding_log_position_working_time
    ADD COLUMN unit_id BIGINT;

ALTER TABLE scaffolding_log_position_working_time
    ADD CONSTRAINT fk_workingtime_unit
        FOREIGN KEY(unit_id) REFERENCES units(id) ON DELETE RESTRICT;

CREATE INDEX idx_workingtime_unit ON scaffolding_log_position_working_time(unit_id);

-- Ustaw domyślną jednostkę r-h dla istniejących rekordów (jeśli istnieją)
UPDATE scaffolding_log_position_working_time
SET unit_id = (
    SELECT id FROM units
    WHERE symbol = 'r-h' AND company_id = scaffolding_log_position_working_time.company_id
    LIMIT 1
    )
WHERE unit_id IS NULL;

-- Teraz możemy dodać NOT NULL constraint
ALTER TABLE scaffolding_log_position_working_time
    ALTER COLUMN unit_id SET NOT NULL;

-- =============================================================================
-- KOMENTARZE
-- =============================================================================
COMMENT ON COLUMN scaffolding_log_position.scaffolding_full_dimension_unit_id IS 'Measurement unit for total scaffolding dimension (m2 or m3) - determined by aggregated dimensions';
COMMENT ON COLUMN scaffolding_log_position_dimensions.unit_id IS 'Measurement unit (m2, m3, mb) - determined by width value';
COMMENT ON COLUMN scaffolding_log_position_working_time.unit_id IS 'Measurement unit for working time (r-h - roboczogodzina)';
