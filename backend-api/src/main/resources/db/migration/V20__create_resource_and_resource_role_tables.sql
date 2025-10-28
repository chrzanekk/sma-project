-- V20__create_resource_and_resource_role_tables.sql
-- Description: Create tables for dynamic resource permission management

-- =============================================================================
-- RESOURCES TABLE - Stores API endpoint definitions
-- =============================================================================
CREATE TABLE resources (
                           id BIGSERIAL PRIMARY KEY,
                           resource_key VARCHAR(100) UNIQUE NOT NULL,
                           endpoint_pattern VARCHAR(255) NOT NULL,
                           description VARCHAR(500),
                           http_method VARCHAR(10),
                           is_active BOOLEAN NOT NULL DEFAULT TRUE,
                           created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT chk_http_method CHECK (
                               http_method IS NULL OR
                               http_method IN ('GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS', 'HEAD')
                               )
);

CREATE INDEX idx_resources_resource_key ON resources(resource_key);
CREATE INDEX idx_resources_is_active ON resources(is_active);

-- =============================================================================
-- RESOURCE_ROLE TABLE
-- =============================================================================
CREATE TABLE resource_role (
                               id BIGSERIAL PRIMARY KEY,
                               resource_id BIGINT NOT NULL,
                               role_id BIGINT NOT NULL,
                               created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT fk_resource_role_resource
                                   FOREIGN KEY (resource_id)
                                       REFERENCES resources(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT fk_resource_role_role
                                   FOREIGN KEY (role_id)
                                       REFERENCES roles(id)
                                       ON DELETE CASCADE,

                               CONSTRAINT uk_resource_role_unique
                                   UNIQUE(resource_id, role_id)
);

CREATE INDEX idx_resource_role_resource ON resource_role(resource_id);
CREATE INDEX idx_resource_role_role ON resource_role(role_id);

-- =============================================================================
-- COMMENTS
-- =============================================================================
COMMENT ON TABLE resources IS 'Stores API endpoint definitions for dynamic permission management';
COMMENT ON COLUMN resources.resource_key IS 'Unique identifier matching ResourceKey enum';
COMMENT ON COLUMN resources.endpoint_pattern IS 'Spring Security request matcher pattern';
COMMENT ON COLUMN resources.http_method IS 'HTTP method restriction (NULL = all methods)';
COMMENT ON COLUMN resources.is_active IS 'Flag to temporarily disable resource without deleting';

COMMENT ON TABLE resource_role IS 'Maps which roles have access to which resources';

-- =============================================================================
-- SEED DATA
-- =============================================================================
INSERT INTO resources (resource_key, endpoint_pattern, description, http_method, is_active)
VALUES
    ('AUTH_PUBLIC', '/api/auth/**', 'Public authentication endpoints', NULL, TRUE),
    ('ACCOUNT_MANAGEMENT', '/api/account/**', 'User account management', NULL, TRUE),
    ('USER_MANAGEMENT', '/api/users/**', 'User administration', NULL, TRUE),
    ('ROLE_MANAGEMENT', '/api/roles/**', 'Role administration', NULL, TRUE),
    ('COMPANY_MANAGEMENT', '/api/companies/**', 'Company management', NULL, TRUE),
    ('CONTRACTOR_MANAGEMENT', '/api/contractors/**', 'Contractor management', NULL, TRUE),
    ('CONTACT_MANAGEMENT', '/api/contacts/**', 'Contact management', NULL, TRUE),
    ('CONSTRUCTION_SITE_MANAGEMENT', '/api/construction-sites/**', 'Construction site management', NULL, TRUE),
    ('POSITION_MANAGEMENT', '/api/positions/**', 'Position management', NULL, TRUE),
    ('CONTRACT_MANAGEMENT', '/api/contracts/**', 'Contract management', NULL, TRUE),
    ('RESOURCE_MANAGEMENT', '/api/resources/**', 'Resource permission management', NULL, TRUE);

-- =============================================================================
-- DEFAULT ROLE ASSIGNMENTS - ✅ POPRAWIONA WERSJA
-- =============================================================================
DO $$
DECLARE
v_role_admin_id BIGINT;
    v_role_user_id BIGINT;
    v_resource_rec RECORD;
BEGIN
    -- Find ROLE_ADMIN and ROLE_USER IDs
SELECT id INTO v_role_admin_id FROM roles WHERE name = 'ROLE_ADMIN';
SELECT id INTO v_role_user_id FROM roles WHERE name = 'ROLE_USER';

-- If roles don't exist, skip
IF v_role_admin_id IS NULL OR v_role_user_id IS NULL THEN
        RAISE NOTICE 'Roles not found - skipping default permissions';
        RETURN;
END IF;

    -- ADMIN has access to everything except public auth
FOR v_resource_rec IN
SELECT id FROM resources WHERE resource_key != 'AUTH_PUBLIC'
    LOOP
INSERT INTO resource_role (resource_id, role_id)
VALUES (v_resource_rec.id, v_role_admin_id)  -- ✅ Użyj v_resource_rec.id
ON CONFLICT (resource_id, role_id) DO NOTHING;
END LOOP;

    -- USER has access to specific resources
FOR v_resource_rec IN
SELECT id FROM resources
WHERE resource_key IN (
                       'ACCOUNT_MANAGEMENT',
                       'CONTRACTOR_MANAGEMENT',
                       'CONTACT_MANAGEMENT',
                       'CONSTRUCTION_SITE_MANAGEMENT',
                       'POSITION_MANAGEMENT',
                       'CONTRACT_MANAGEMENT'
    )
    LOOP
INSERT INTO resource_role (resource_id, role_id)
VALUES (v_resource_rec.id, v_role_user_id)  -- ✅ Użyj v_resource_rec.id
ON CONFLICT (resource_id, role_id) DO NOTHING;
END LOOP;

    RAISE NOTICE 'Default permissions assigned successfully';
END $$;
