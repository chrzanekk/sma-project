-- V31__add_last_login_to_users.sql
-- Dodaje pole last_login_datetime do tabeli users

ALTER TABLE users
    ADD COLUMN last_login_datetime TIMESTAMP WITH TIME ZONE;