CREATE TABLE scaffolding_number_counter (
                                            id bigserial PRIMARY KEY,
                                            scaffolding_log_id bigint NOT NULL,
                                            year int NOT NULL,
                                            last_number int NOT NULL,
                                            CONSTRAINT fk_counter_log FOREIGN KEY (scaffolding_log_id) REFERENCES scaffolding_log(id) ON DELETE CASCADE,
                                            CONSTRAINT uq_log_year UNIQUE (scaffolding_log_id, year)
);

COMMENT ON TABLE scaffolding_number_counter IS 'Przechowuje ostatni użyty główny numer rusztowania dla danego dziennika i roku';