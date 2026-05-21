-- V24__fix_scaffolding_full_dimension_precision.sql
-- Description: Fix working time columns - workers as integer, full dimension with 4 decimal places

-- Fix scaffolding_log_position - full dimension needs 4 decimal places
ALTER TABLE scaffolding_log_position
ALTER
COLUMN scaffolding_full_dimension TYPE NUMERIC(19,4);

-- Fix scaffolding_log_position_working_time
-- Change number_of_workers from NUMERIC to INTEGER (whole numbers only)
ALTER TABLE scaffolding_log_position_working_time
ALTER
COLUMN number_of_workers TYPE INTEGER USING number_of_workers::INTEGER;

      -- Update comments
COMMENT ON COLUMN scaffolding_log_position.scaffolding_full_dimension IS 'Total dimension of scaffolding (height × width × length) with 4 decimal places precision';
COMMENT ON COLUMN scaffolding_log_position_working_time.number_of_workers IS 'Number of workers (whole number: 1, 2, 3, etc.)';

