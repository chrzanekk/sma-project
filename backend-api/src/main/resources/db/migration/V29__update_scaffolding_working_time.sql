-- V29__update_scaffolding_working_time.sql
-- Description: Rename full_working_time to partial, add new full_working_time

-- =============================================================================
-- 1. ZMIANA NAZWY ISTNIEJĄCEGO CZASU PRACY (FULL -> PARTIAL)
-- =============================================================================
ALTER TABLE scaffolding_log_position
    RENAME COLUMN full_working_time TO partial_working_time;

-- =============================================================================
-- 2. DODANIE NOWEJ KOLUMNY DLA FULL WORKING TIME
-- =============================================================================
ALTER TABLE scaffolding_log_position
    ADD COLUMN full_working_time NUMERIC(19,4) DEFAULT 0;

-- Wyrównanie wartości NULL w nowej kolumnie (bezpieczeństwo)
UPDATE scaffolding_log_position
SET full_working_time = 0
WHERE full_working_time IS NULL;

-- Uwaga: W przeciwieństwie do migracji dla 'dimensions', nie dodajemy tutaj nowej kolumny
-- dla 'unit_id' (jednostki), ponieważ z poprzednich skryptów (V26 i encji) wynika,
-- że tabela scaffolding_log_position nie posiadała osobnego klucza obcego
-- dla jednostki 'full_working_time'. Jednostka (r-h) jest określana
-- zazwyczaj globalnie lub bezpośrednio w tabeli scaffolding_log_position_working_time.

-- =============================================================================
-- 3. AKTUALIZACJA KOMENTARZY
-- =============================================================================
COMMENT ON COLUMN scaffolding_log_position.partial_working_time IS 'Partial working time for the specific scaffolding position (previously full_working_time).';
COMMENT ON COLUMN scaffolding_log_position.full_working_time IS 'Total aggregated working time for the scaffolding position and all its children (can be null, default 0).';