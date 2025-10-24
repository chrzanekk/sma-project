-- Dodanie kolumny position_id do tabeli users
ALTER TABLE users
    ADD COLUMN position_id BIGINT;

-- Ustawienie klucza obcego do tabeli positions
ALTER TABLE users
    ADD CONSTRAINT fk_users_position
        FOREIGN KEY (position_id)
            REFERENCES positions (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;

-- (Opcjonalnie) przeniesienie danych ze starej kolumny position, jeśli istnieją
-- UPDATE users u
-- SET position_id = p.id
-- FROM positions p
-- WHERE p.name = u.position;

-- (Opcjonalnie) usunięcie starej kolumny po migracji danych
ALTER TABLE users DROP COLUMN position;

-- Indeks dla kolumny position_id (dobra praktyka wydajnościowa)
CREATE INDEX IF NOT EXISTS idx_users_position_id ON users (position_id);
