ALTER TABLE auth.users ADD COLUMN auth_provider varchar(16) NOT NULL DEFAULT 'LOCAL';
