-- V27__add_full_working_time_to_scaffolding_log_position.sql
-- Description: Add full_working_time column to scaffolding_log_position

ALTER TABLE scaffolding_log_position
    ADD COLUMN full_working_time NUMERIC(19,4) DEFAULT 0;

-- Opcjonalnie, jeśli chcesz jawnie zaktualizować istniejące rekordy
UPDATE scaffolding_log_position
SET full_working_time = 0
WHERE full_working_time IS NULL;

COMMENT
ON COLUMN scaffolding_log_position.full_working_time IS 'Aggregated working time for the scaffolding position (can be null, default 0).';
