ALTER TABLE contacts ADD COLUMN created_by BIGINT;
ALTER TABLE contacts ADD COLUMN modified_by BIGINT;

ALTER TABLE contacts ADD CONSTRAINT fk_created_by FOREIGN KEY (created_by) REFERENCES users (id);
ALTER TABLE contacts ADD CONSTRAINT fk_modified_by FOREIGN KEY (modified_by) REFERENCES users (id);