ALTER TABLE auth.users ADD COLUMN auth_provider varchar(16) NOT NULL DEFAULT 'LOCAL';

UPDATE auth.users SET auth_provider = 'LOCAL' WHERE auth_provider IS NULL;
