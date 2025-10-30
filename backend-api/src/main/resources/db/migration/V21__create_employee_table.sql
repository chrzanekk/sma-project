-- V21__create_employee_table.sql
-- Description: Create employee table with relationships to position and company

-- =============================================================================
-- EMPLOYEE SEQUENCE
-- =============================================================================
CREATE SEQUENCE employees_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- =============================================================================
-- EMPLOYEE TABLE
-- =============================================================================
CREATE TABLE employee (
                          id BIGINT PRIMARY KEY DEFAULT nextval('employees_sequence'),
                          first_name VARCHAR(100),
                          last_name VARCHAR(100),
                          hour_rate NUMERIC(10,2),
                          position_id BIGINT NOT NULL,
                          company_id BIGINT NOT NULL,
                          create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          created_by VARCHAR(255),
                          modify_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          modified_by VARCHAR(255),

                          CONSTRAINT fk_employee_position
                              FOREIGN KEY (position_id)
                                  REFERENCES positions(id)
                                  ON DELETE RESTRICT,

                          CONSTRAINT fk_employee_company
                              FOREIGN KEY (company_id)
                                  REFERENCES company(id)
                                  ON DELETE RESTRICT,

                          CONSTRAINT chk_hour_rate_positive
                              CHECK (hour_rate IS NULL OR hour_rate >= 0)
);

-- =============================================================================
-- INDEXES
-- =============================================================================
CREATE INDEX idx_employee_position_id ON employee(position_id);
CREATE INDEX idx_employee_company_id ON employee(company_id);
CREATE INDEX idx_employee_last_name ON employee(last_name);
CREATE INDEX idx_employee_created_date ON employee(create_date);

-- =============================================================================
-- COMMENTS
-- =============================================================================
COMMENT ON TABLE employee IS 'Stores employee information with position and company relationships';
COMMENT ON COLUMN employee.id IS 'Primary key - employee identifier';
COMMENT ON COLUMN employee.first_name IS 'Employee first name';
COMMENT ON COLUMN employee.last_name IS 'Employee last name';
COMMENT ON COLUMN employee.hour_rate IS 'Hourly rate for employee services';
COMMENT ON COLUMN employee.position_id IS 'Foreign key to position table';
COMMENT ON COLUMN employee.company_id IS 'Foreign key to company table';
COMMENT ON COLUMN employee.create_date IS 'Timestamp when record was created';
COMMENT ON COLUMN employee.created_by IS 'User who created the record';
COMMENT ON COLUMN employee.modify_date IS 'Timestamp when record was last modified';
COMMENT ON COLUMN employee.modified_by IS 'User who last modified the record';

-- =============================================================================
-- GRANTS (optional - adjust according to your security model)
-- =============================================================================
-- GRANT SELECT, INSERT, UPDATE, DELETE ON employee TO sma_app_user;
-- GRANT USAGE, SELECT ON SEQUENCE employees_sequence TO sma_app_user;
