-- Migracja dla tabeli contractors
DO $$
    BEGIN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'contractors' AND column_name = 'id') THEN
            -- Usuń właściwość IDENTITY z kolumny
            ALTER TABLE contractors ALTER COLUMN id DROP IDENTITY IF EXISTS;

            -- Utwórz sekwencję, jeśli nie istnieje
            IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'contractor_sequence') THEN
                CREATE SEQUENCE contractor_sequence;
            END IF;

            -- Ustaw sekwencję jako źródło wartości dla kolumny
            ALTER TABLE contractors ALTER COLUMN id SET DEFAULT nextval('contractor_sequence');

            -- Zaktualizuj wartość sekwencji na podstawie maksymalnego istniejącego id
            PERFORM SETVAL('contractor_sequence', COALESCE((SELECT MAX(id) FROM contractors), 1));
        END IF;
    END$$;

-- Migracja dla tabeli send_emails
DO $$
    BEGIN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'send_emails' AND column_name = 'id') THEN
            ALTER TABLE send_emails ALTER COLUMN id DROP IDENTITY IF EXISTS;

            IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'send_emails_sequence') THEN
                CREATE SEQUENCE send_emails_sequence;
            END IF;

            ALTER TABLE send_emails ALTER COLUMN id SET DEFAULT nextval('send_emails_sequence');
            PERFORM SETVAL('send_emails_sequence', COALESCE((SELECT MAX(id) FROM send_emails), 1));
        END IF;
    END$$;

-- Migracja dla tabeli roles
DO $$
    BEGIN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'roles' AND column_name = 'id') THEN
            ALTER TABLE roles ALTER COLUMN id DROP IDENTITY IF EXISTS;

            IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'roles_sequence') THEN
                CREATE SEQUENCE roles_sequence;
            END IF;

            ALTER TABLE roles ALTER COLUMN id SET DEFAULT nextval('roles_sequence');
            PERFORM SETVAL('roles_sequence', COALESCE((SELECT MAX(id) FROM roles), 1));
        END IF;
    END$$;

-- Migracja dla tabeli users
DO $$
    BEGIN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'users' AND column_name = 'id') THEN
            ALTER TABLE users ALTER COLUMN id DROP IDENTITY IF EXISTS;

            IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'users_sequence') THEN
                CREATE SEQUENCE users_sequence;
            END IF;

            ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_sequence');
            PERFORM SETVAL('users_sequence', COALESCE((SELECT MAX(id) FROM users), 1));
        END IF;
    END$$;

-- Migracja dla tabeli user_tokens
DO $$
    BEGIN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'user_tokens' AND column_name = 'id') THEN
            ALTER TABLE user_tokens ALTER COLUMN id DROP IDENTITY IF EXISTS;

            IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'user_tokens_sequence') THEN
                CREATE SEQUENCE user_tokens_sequence;
            END IF;

            ALTER TABLE user_tokens ALTER COLUMN id SET DEFAULT nextval('user_tokens_sequence');
            PERFORM SETVAL('user_tokens_sequence', COALESCE((SELECT MAX(id) FROM user_tokens), 1));
        END IF;
    END$$;
