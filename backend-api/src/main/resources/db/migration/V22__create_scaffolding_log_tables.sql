-- V22__create_scaffolding_log_tables.sql
-- Description: Create tables for scaffolding log management system with company separation

-- =============================================================================
-- SEQUENCES
-- =============================================================================
CREATE SEQUENCE work_type_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE scaffolding_log_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE scaffolding_log_position_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE scaffolding_log_position_working_time_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE scaffolding_log_position_dimensions_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- =============================================================================
-- WORK_TYPE TABLE
-- =============================================================================
CREATE TABLE work_type (
                           id BIGINT PRIMARY KEY DEFAULT nextval('work_type_sequence'),
                           name VARCHAR(255) NOT NULL,
                           description VARCHAR(500),
                           company_id BIGINT NOT NULL,
                           created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           created_by VARCHAR(255),
                           modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           modified_by VARCHAR(255),

                           CONSTRAINT fk_work_type_company
                               FOREIGN KEY (company_id)
                                   REFERENCES company(id)
                                   ON DELETE RESTRICT
);

CREATE INDEX idx_work_type_name ON work_type(name);
CREATE INDEX idx_work_type_company ON work_type(company_id);
CREATE INDEX idx_work_type_company_name ON work_type(company_id, name);

-- =============================================================================
-- SCAFFOLDING_LOG TABLE
-- =============================================================================
CREATE TABLE scaffolding_log (
                                 id BIGINT PRIMARY KEY DEFAULT nextval('scaffolding_log_sequence'),
                                 construction_site_id BIGINT NOT NULL,
                                 contractor_id BIGINT NOT NULL,
                                 name VARCHAR(255) NOT NULL,
                                 additional_info VARCHAR(500) NOT NULL,
                                 company_id BIGINT NOT NULL,
                                 created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 created_by VARCHAR(255),
                                 modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 modified_by VARCHAR(255),

                                 CONSTRAINT fk_scaffolding_log_construction_site
                                     FOREIGN KEY (construction_site_id)
                                         REFERENCES construction_site(id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT fk_scaffolding_log_contractor
                                     FOREIGN KEY (contractor_id)
                                         REFERENCES contractors(id)
                                         ON DELETE RESTRICT,

                                 CONSTRAINT fk_scaffolding_log_company
                                     FOREIGN KEY (company_id)
                                         REFERENCES company(id)
                                         ON DELETE RESTRICT
);

CREATE INDEX idx_scaffolding_log_construction_site ON scaffolding_log(construction_site_id);
CREATE INDEX idx_scaffolding_log_contractor ON scaffolding_log(contractor_id);
CREATE INDEX idx_scaffolding_log_company ON scaffolding_log(company_id);
CREATE INDEX idx_scaffolding_log_company_name ON scaffolding_log(company_id, name);

-- =============================================================================
-- SCAFFOLDING_LOG_POSITION TABLE
-- =============================================================================
CREATE TABLE scaffolding_log_position (
                                          id BIGINT PRIMARY KEY DEFAULT nextval('scaffolding_log_position_sequence'),
                                          scaffolding_log_id BIGINT NOT NULL,
                                          scaffolding_parent_id BIGINT,
                                          scaffolding_number VARCHAR(50) NOT NULL,
                                          contractor_id BIGINT NOT NULL,
                                          contractor_contact_id BIGINT NOT NULL,
                                          scaffolding_user_id BIGINT NOT NULL,
                                          scaffolding_user_contact_id BIGINT NOT NULL,
                                          assembly_location VARCHAR(500),
                                          assembly_date TIMESTAMP,
                                          dismantling_date TIMESTAMP,
                                          dismantling_notification_date TIMESTAMP,
                                          scaffolding_type VARCHAR(50),
                                          scaffolding_full_dimension NUMERIC(15,2),
                                          technical_protocol_status VARCHAR(50),
                                          company_id BIGINT NOT NULL,
                                          created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          created_by VARCHAR(255),
                                          modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          modified_by VARCHAR(255),

                                          CONSTRAINT fk_scaffolding_position_log
                                              FOREIGN KEY (scaffolding_log_id)
                                                  REFERENCES scaffolding_log(id)
                                                  ON DELETE RESTRICT,

                                          CONSTRAINT fk_scaffolding_position_parent
                                              FOREIGN KEY (scaffolding_parent_id)
                                                  REFERENCES scaffolding_log_position(id)
                                                  ON DELETE RESTRICT,

                                          CONSTRAINT fk_scaffolding_position_contractor
                                              FOREIGN KEY (contractor_id)
                                                  REFERENCES contractors(id)
                                                  ON DELETE RESTRICT,

                                          CONSTRAINT fk_scaffolding_position_contractor_contact
                                              FOREIGN KEY (contractor_contact_id)
                                                  REFERENCES contacts(id)
                                                  ON DELETE RESTRICT,

                                          CONSTRAINT fk_scaffolding_position_user
                                              FOREIGN KEY (scaffolding_user_id)
                                                  REFERENCES contractors(id)
                                                  ON DELETE RESTRICT,

                                          CONSTRAINT fk_scaffolding_position_user_contact
                                              FOREIGN KEY (scaffolding_user_contact_id)
                                                  REFERENCES contacts(id)
                                                  ON DELETE RESTRICT,

                                          CONSTRAINT fk_scaffolding_position_company
                                              FOREIGN KEY (company_id)
                                                  REFERENCES company(id)
                                                  ON DELETE RESTRICT
);

CREATE INDEX idx_scaffolding_position_log ON scaffolding_log_position(scaffolding_log_id);
CREATE INDEX idx_scaffolding_position_parent ON scaffolding_log_position(scaffolding_parent_id);
CREATE INDEX idx_scaffolding_position_number ON scaffolding_log_position(scaffolding_number);
CREATE INDEX idx_scaffolding_position_contractor ON scaffolding_log_position(contractor_id);
CREATE INDEX idx_scaffolding_position_company ON scaffolding_log_position(company_id);
CREATE INDEX idx_scaffolding_position_assembly_date ON scaffolding_log_position(assembly_date);
CREATE INDEX idx_scaffolding_position_dismantling_date ON scaffolding_log_position(dismantling_date);
CREATE INDEX idx_scaffolding_position_company_number ON scaffolding_log_position(company_id, scaffolding_number);
CREATE INDEX idx_scaffolding_position_company_assembly ON scaffolding_log_position(company_id, assembly_date);

-- =============================================================================
-- SCAFFOLDING_LOG_POSITION_WORKING_TIME TABLE
-- =============================================================================
CREATE TABLE scaffolding_log_position_working_time (
                                                       id BIGINT PRIMARY KEY DEFAULT nextval('scaffolding_log_position_working_time_sequence'),
                                                       scaffolding_position_id BIGINT NOT NULL,
                                                       number_of_workers NUMERIC(10,2),
                                                       number_of_hours NUMERIC(10,2),
                                                       work_type_id BIGINT,
                                                       company_id BIGINT NOT NULL,
                                                       created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                       created_by VARCHAR(255),
                                                       modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                       modified_by VARCHAR(255),

                                                       CONSTRAINT fk_working_time_position
                                                           FOREIGN KEY (scaffolding_position_id)
                                                               REFERENCES scaffolding_log_position(id)
                                                               ON DELETE CASCADE,

                                                       CONSTRAINT fk_working_time_work_type
                                                           FOREIGN KEY (work_type_id)
                                                               REFERENCES work_type(id)
                                                               ON DELETE SET NULL,

                                                       CONSTRAINT fk_working_time_company
                                                           FOREIGN KEY (company_id)
                                                               REFERENCES company(id)
                                                               ON DELETE RESTRICT
);

CREATE INDEX idx_working_time_position ON scaffolding_log_position_working_time(scaffolding_position_id);
CREATE INDEX idx_working_time_work_type ON scaffolding_log_position_working_time(work_type_id);
CREATE INDEX idx_working_time_company ON scaffolding_log_position_working_time(company_id);
CREATE INDEX idx_working_time_company_position ON scaffolding_log_position_working_time(company_id, scaffolding_position_id);

-- =============================================================================
-- SCAFFOLDING_LOG_POSITION_DIMENSIONS TABLE
-- =============================================================================
CREATE TABLE scaffolding_log_position_dimensions (
                                                     id BIGINT PRIMARY KEY DEFAULT nextval('scaffolding_log_position_dimensions_sequence'),
                                                     scaffolding_position_id BIGINT NOT NULL,
                                                     height NUMERIC(10,2),
                                                     width NUMERIC(10,2),
                                                     length NUMERIC(10,2),
                                                     dimension_type VARCHAR(50) NOT NULL DEFAULT 'konstrukcja podstawowa',
                                                     work_type_id BIGINT NOT NULL,
                                                     dismantling_date TIMESTAMP,
                                                     assembly_date TIMESTAMP,
                                                     company_id BIGINT NOT NULL,
                                                     created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                     created_by VARCHAR(255),
                                                     modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                     modified_by VARCHAR(255),

                                                     CONSTRAINT fk_dimensions_position
                                                         FOREIGN KEY (scaffolding_position_id)
                                                             REFERENCES scaffolding_log_position(id)
                                                             ON DELETE CASCADE,

                                                     CONSTRAINT fk_dimensions_work_type
                                                         FOREIGN KEY (work_type_id)
                                                             REFERENCES work_type(id)
                                                             ON DELETE RESTRICT,

                                                     CONSTRAINT fk_dimensions_company
                                                         FOREIGN KEY (company_id)
                                                             REFERENCES company(id)
                                                             ON DELETE RESTRICT
);

CREATE INDEX idx_dimensions_position ON scaffolding_log_position_dimensions(scaffolding_position_id);
CREATE INDEX idx_dimensions_work_type ON scaffolding_log_position_dimensions(work_type_id);
CREATE INDEX idx_dimensions_type ON scaffolding_log_position_dimensions(dimension_type);
CREATE INDEX idx_dimensions_dismantling_date ON scaffolding_log_position_dimensions(dismantling_date);
CREATE INDEX idx_dimensions_company ON scaffolding_log_position_dimensions(company_id);
CREATE INDEX idx_dimensions_company_position ON scaffolding_log_position_dimensions(company_id, scaffolding_position_id);

-- =============================================================================
-- COMMENTS
-- =============================================================================
COMMENT ON TABLE work_type IS 'Stores work type definitions for scaffolding operations - company separated';
COMMENT ON COLUMN work_type.name IS 'Work type name';
COMMENT ON COLUMN work_type.description IS 'Optional work type description';
COMMENT ON COLUMN work_type.company_id IS 'Foreign key to company table - ensures data separation';

COMMENT ON TABLE scaffolding_log IS 'Main scaffolding log table linked to construction sites - company separated';
COMMENT ON COLUMN scaffolding_log.construction_site_id IS 'Foreign key to construction_sites table';
COMMENT ON COLUMN scaffolding_log.contractor_id IS 'Foreign key to contractors table';
COMMENT ON COLUMN scaffolding_log.name IS 'Scaffolding log name';
COMMENT ON COLUMN scaffolding_log.additional_info IS 'Additional information (e.g., department at ZAP) - max 500 characters';
COMMENT ON COLUMN scaffolding_log.company_id IS 'Foreign key to company table - ensures data separation';

COMMENT ON TABLE scaffolding_log_position IS 'Individual scaffolding positions within a log - company separated';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_log_id IS 'Foreign key to scaffolding_log table';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_parent_id IS 'Parent position ID for expansions or modifications - NULL for initial scaffolding';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_number IS 'Scaffolding number format: 1/25/01/2025 (number/day/month/year)';
COMMENT ON COLUMN scaffolding_log_position.contractor_id IS 'Ordering party contractor';
COMMENT ON COLUMN scaffolding_log_position.contractor_contact_id IS 'Ordering party contact';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_user_id IS 'Scaffolding user (company) - copies from contractor_id if not specified';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_user_contact_id IS 'Scaffolding user contact - copies from contractor_contact_id if not specified';
COMMENT ON COLUMN scaffolding_log_position.assembly_location IS 'Scaffolding assembly location';
COMMENT ON COLUMN scaffolding_log_position.assembly_date IS 'Date of scaffolding assembly';
COMMENT ON COLUMN scaffolding_log_position.dismantling_date IS 'Date of complete dismantling - auto-filled when last dimension is dismantled';
COMMENT ON COLUMN scaffolding_log_position.dismantling_notification_date IS 'Date of dismantling notification - important for rental billing';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_type IS 'Type of scaffolding: podwieszane, jezdne, podstawowe';
COMMENT ON COLUMN scaffolding_log_position.scaffolding_full_dimension IS 'Total dimension of scaffolding including sub-scaffolding';
COMMENT ON COLUMN scaffolding_log_position.technical_protocol_status IS 'Technical acceptance protocol status';
COMMENT ON COLUMN scaffolding_log_position.company_id IS 'Foreign key to company table - ensures data separation';

COMMENT ON TABLE scaffolding_log_position_working_time IS 'Working time records for scaffolding positions - company separated';
COMMENT ON COLUMN scaffolding_log_position_working_time.scaffolding_position_id IS 'Foreign key to scaffolding_log_position table';
COMMENT ON COLUMN scaffolding_log_position_working_time.number_of_workers IS 'Number of workers';
COMMENT ON COLUMN scaffolding_log_position_working_time.number_of_hours IS 'Number of hours worked';
COMMENT ON COLUMN scaffolding_log_position_working_time.work_type_id IS 'Optional work type - can be general time for multiple activities';
COMMENT ON COLUMN scaffolding_log_position_working_time.company_id IS 'Foreign key to company table - ensures data separation';

COMMENT ON TABLE scaffolding_log_position_dimensions IS 'Dimension records for scaffolding positions - multiple dimensions per position - company separated';
COMMENT ON COLUMN scaffolding_log_position_dimensions.scaffolding_position_id IS 'Foreign key to scaffolding_log_position table';
COMMENT ON COLUMN scaffolding_log_position_dimensions.height IS 'Height in meters (e.g., 3.5)';
COMMENT ON COLUMN scaffolding_log_position_dimensions.width IS 'Width in meters (e.g., 6.14)';
COMMENT ON COLUMN scaffolding_log_position_dimensions.length IS 'Length in meters (e.g., 6.14)';
COMMENT ON COLUMN scaffolding_log_position_dimensions.dimension_type IS 'Dimension type: balkon/konsola, podwieszenie, dźwigary, konstrukcja podstawowa (default)';
COMMENT ON COLUMN scaffolding_log_position_dimensions.work_type_id IS 'Foreign key to work_type table';
COMMENT ON COLUMN scaffolding_log_position_dimensions.dismantling_date IS 'Date of partial dismantling for this dimension - last dimension dismantled triggers full position dismantling';
COMMENT ON COLUMN scaffolding_log_position_dimensions.company_id IS 'Foreign key to company table - ensures data separation';

