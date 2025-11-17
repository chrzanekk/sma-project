-- V23__fix_audit_columns_type_and_names.sql
-- Description: Fix audit columns - change type from VARCHAR to BIGINT, rename date columns, and add foreign keys to users table

-- =============================================================================
-- FIX SCAFFOLDING_LOG TABLE
-- =============================================================================

-- Step 1: Rename modify_date to match naming convention
ALTER TABLE scaffolding_log
    RENAME COLUMN modified_date TO modify_date;
ALTER TABLE scaffolding_log
    RENAME COLUMN created_date TO create_date;

-- Step 2: Alter column types from VARCHAR to BIGINT
ALTER TABLE scaffolding_log
ALTER COLUMN created_by TYPE BIGINT USING (
        CASE
            WHEN created_by IS NULL THEN NULL
            WHEN created_by ~ '^[0-9]+$' THEN created_by::BIGINT
            ELSE NULL
        END
    );

ALTER TABLE scaffolding_log
ALTER COLUMN modified_by TYPE BIGINT USING (
        CASE
            WHEN modified_by IS NULL THEN NULL
            WHEN modified_by ~ '^[0-9]+$' THEN modified_by::BIGINT
            ELSE NULL
        END
    );

-- Step 4: Add foreign key constraints
ALTER TABLE scaffolding_log
    ADD CONSTRAINT fk_scaffolding_log_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE scaffolding_log
    ADD CONSTRAINT fk_scaffolding_log_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);

-- =============================================================================
-- FIX EMPLOYEE TABLE
-- =============================================================================

-- Step 1: Alter column types from VARCHAR to BIGINT
ALTER TABLE employee
ALTER COLUMN created_by TYPE BIGINT USING (
        CASE
            WHEN created_by IS NULL THEN NULL
            WHEN created_by ~ '^[0-9]+$' THEN created_by::BIGINT
            ELSE NULL
        END
    );

ALTER TABLE employee
ALTER COLUMN modified_by TYPE BIGINT USING (
        CASE
            WHEN modified_by IS NULL THEN NULL
            WHEN modified_by ~ '^[0-9]+$' THEN modified_by::BIGINT
            ELSE NULL
        END
    );

-- Step 2: Add foreign key constraints
ALTER TABLE employee
    ADD CONSTRAINT fk_employee_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE employee
    ADD CONSTRAINT fk_employee_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);

-- =============================================================================
-- FIX SCAFFOLDING_LOG_POSITION TABLE
-- =============================================================================

-- Step 1: Rename date columns to match naming convention
ALTER TABLE scaffolding_log_position
    RENAME COLUMN created_date TO create_date;

ALTER TABLE scaffolding_log_position
    RENAME COLUMN modified_date TO modify_date;

-- Step 2: Alter column types from VARCHAR to BIGINT
ALTER TABLE scaffolding_log_position
ALTER COLUMN created_by TYPE BIGINT USING (
        CASE
            WHEN created_by IS NULL THEN NULL
            WHEN created_by ~ '^[0-9]+$' THEN created_by::BIGINT
            ELSE NULL
        END
    );

ALTER TABLE scaffolding_log_position
ALTER COLUMN modified_by TYPE BIGINT USING (
        CASE
            WHEN modified_by IS NULL THEN NULL
            WHEN modified_by ~ '^[0-9]+$' THEN modified_by::BIGINT
            ELSE NULL
        END
    );

-- Step 3: Add foreign key constraints
ALTER TABLE scaffolding_log_position
    ADD CONSTRAINT fk_scaffolding_log_position_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE scaffolding_log_position
    ADD CONSTRAINT fk_scaffolding_log_position_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);

-- =============================================================================
-- FIX SCAFFOLDING_LOG_POSITION_WORKING_TIME TABLE
-- =============================================================================

-- Step 1: Rename date columns to match naming convention
ALTER TABLE scaffolding_log_position_working_time
    RENAME COLUMN created_date TO create_date;

ALTER TABLE scaffolding_log_position_working_time
    RENAME COLUMN modified_date TO modify_date;

-- Step 2: Alter column types from VARCHAR to BIGINT
ALTER TABLE scaffolding_log_position_working_time
ALTER COLUMN created_by TYPE BIGINT USING (
        CASE
            WHEN created_by IS NULL THEN NULL
            WHEN created_by ~ '^[0-9]+$' THEN created_by::BIGINT
            ELSE NULL
        END
    );

ALTER TABLE scaffolding_log_position_working_time
ALTER COLUMN modified_by TYPE BIGINT USING (
        CASE
            WHEN modified_by IS NULL THEN NULL
            WHEN modified_by ~ '^[0-9]+$' THEN modified_by::BIGINT
            ELSE NULL
        END
    );

-- Step 3: Add foreign key constraints
ALTER TABLE scaffolding_log_position_working_time
    ADD CONSTRAINT fk_working_time_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE scaffolding_log_position_working_time
    ADD CONSTRAINT fk_working_time_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);

-- =============================================================================
-- FIX SCAFFOLDING_LOG_POSITION_DIMENSIONS TABLE
-- =============================================================================

-- Step 1: Rename date columns to match naming convention
ALTER TABLE scaffolding_log_position_dimensions
    RENAME COLUMN created_date TO create_date;

ALTER TABLE scaffolding_log_position_dimensions
    RENAME COLUMN modified_date TO modify_date;

-- Step 2: Alter column types from VARCHAR to BIGINT
ALTER TABLE scaffolding_log_position_dimensions
ALTER COLUMN created_by TYPE BIGINT USING (
        CASE
            WHEN created_by IS NULL THEN NULL
            WHEN created_by ~ '^[0-9]+$' THEN created_by::BIGINT
            ELSE NULL
        END
    );

ALTER TABLE scaffolding_log_position_dimensions
ALTER COLUMN modified_by TYPE BIGINT USING (
        CASE
            WHEN modified_by IS NULL THEN NULL
            WHEN modified_by ~ '^[0-9]+$' THEN modified_by::BIGINT
            ELSE NULL
        END
    );

-- Step 3: Add foreign key constraints
ALTER TABLE scaffolding_log_position_dimensions
    ADD CONSTRAINT fk_dimensions_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE scaffolding_log_position_dimensions
    ADD CONSTRAINT fk_dimensions_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);

-- =============================================================================
-- UPDATE INDEXES (if needed - PostgreSQL automatically updates indexes on RENAME)
-- =============================================================================
-- Note: PostgreSQL automatically updates index references when columns are renamed
-- No manual index recreation is needed

-- =============================================================================
-- COMMENTS
-- =============================================================================
COMMENT ON COLUMN scaffolding_log.create_date IS 'Timestamp when record was created';
COMMENT ON COLUMN scaffolding_log.modify_date IS 'Timestamp when record was last modified';
COMMENT ON COLUMN scaffolding_log.created_by IS 'Foreign key to users table - user who created the record';
COMMENT ON COLUMN scaffolding_log.modified_by IS 'Foreign key to users table - user who last modified the record';

COMMENT ON COLUMN employee.created_by IS 'Foreign key to users table - user who created the record';
COMMENT ON COLUMN employee.modified_by IS 'Foreign key to users table - user who last modified the record';

COMMENT ON COLUMN scaffolding_log_position.create_date IS 'Timestamp when record was created';
COMMENT ON COLUMN scaffolding_log_position.modify_date IS 'Timestamp when record was last modified';
COMMENT ON COLUMN scaffolding_log_position.created_by IS 'Foreign key to users table - user who created the record';
COMMENT ON COLUMN scaffolding_log_position.modified_by IS 'Foreign key to users table - user who last modified the record';

COMMENT ON COLUMN scaffolding_log_position_working_time.create_date IS 'Timestamp when record was created';
COMMENT ON COLUMN scaffolding_log_position_working_time.modify_date IS 'Timestamp when record was last modified';
COMMENT ON COLUMN scaffolding_log_position_working_time.created_by IS 'Foreign key to users table - user who created the record';
COMMENT ON COLUMN scaffolding_log_position_working_time.modified_by IS 'Foreign key to users table - user who last modified the record';

COMMENT ON COLUMN scaffolding_log_position_dimensions.create_date IS 'Timestamp when record was created';
COMMENT ON COLUMN scaffolding_log_position_dimensions.modify_date IS 'Timestamp when record was last modified';
COMMENT ON COLUMN scaffolding_log_position_dimensions.created_by IS 'Foreign key to users table - user who created the record';
COMMENT ON COLUMN scaffolding_log_position_dimensions.modified_by IS 'Foreign key to users table - user who last modified the record';
