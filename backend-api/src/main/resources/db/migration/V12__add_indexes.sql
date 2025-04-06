-- CONTRACTORS
CREATE INDEX IF NOT EXISTS idx_contractors_company_id ON contractors (company_id);
CREATE INDEX IF NOT EXISTS idx_contractors_created_by ON contractors (created_by);
CREATE INDEX IF NOT EXISTS idx_contractors_modified_by ON contractors (modified_by);
CREATE INDEX IF NOT EXISTS idx_contractors_name ON contractors (name);

-- CONTACTS
CREATE INDEX IF NOT EXISTS idx_contacts_company_id ON contacts (company_id);
CREATE INDEX IF NOT EXISTS idx_contacts_contractor_id ON contacts (contractor_id);
CREATE INDEX IF NOT EXISTS idx_contacts_created_by ON contacts (created_by);
CREATE INDEX IF NOT EXISTS idx_contacts_modified_by ON contacts (modified_by);
CREATE INDEX IF NOT EXISTS idx_contacts_email ON contacts (email);

-- USERS
CREATE INDEX IF NOT EXISTS idx_users_first_name ON users (first_name);
CREATE INDEX IF NOT EXISTS idx_users_last_name ON users (last_name);
CREATE INDEX IF NOT EXISTS idx_users_locked ON users (locked);
CREATE INDEX IF NOT EXISTS idx_users_enabled ON users (enabled);

-- USER_COMPANIES
CREATE INDEX IF NOT EXISTS idx_user_companies_user_id ON user_companies (user_id);
CREATE INDEX IF NOT EXISTS idx_user_companies_company_id ON user_companies (company_id);

-- USER_ROLES
CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles (role_id);
