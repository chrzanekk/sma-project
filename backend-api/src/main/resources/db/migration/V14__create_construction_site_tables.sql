CREATE SEQUENCE construction_site_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE construction_site
(
    id          BIGINT       NOT NULL DEFAULT nextval('construction_site_sequence'),
    name        VARCHAR(200) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    country     VARCHAR(255),
    short_name  VARCHAR(255),
    code        VARCHAR(3),
    company_id  BIGINT       NOT NULL,
    create_date TIMESTAMP WITHOUT TIME ZONE,
    modify_date TIMESTAMP WITHOUT TIME ZONE,
    created_by  BIGINT,
    modified_by BIGINT,
    CONSTRAINT construction_site_pkey PRIMARY KEY (id)
);

ALTER TABLE construction_site
    ADD CONSTRAINT fk_construction_site_company
        FOREIGN KEY (company_id) REFERENCES company (id);

ALTER TABLE construction_site
    ADD CONSTRAINT fk_construction_site_created_by
        FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE construction_site
    ADD CONSTRAINT fk_construction_site_modified_by
        FOREIGN KEY (modified_by) REFERENCES users (id);


CREATE TABLE construction_site_contractor
(
    construction_site_id BIGINT NOT NULL,
    contractor_id        BIGINT NOT NULL,
    CONSTRAINT pk_construction_site_contractor
        PRIMARY KEY (construction_site_id, contractor_id),
    CONSTRAINT fk_csc_site
        FOREIGN KEY (construction_site_id) REFERENCES construction_site (id),
    CONSTRAINT fk_csc_contractor
        FOREIGN KEY (contractor_id) REFERENCES contractors (id)
);
