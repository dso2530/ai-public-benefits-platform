CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE applications (
    id UUID PRIMARY KEY,
    application_id UUID NOT NULL,
    subject VARCHAR(255) NOT NULL,
    aid_code VARCHAR(255) NOT NULL,
    aid_name VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    object_key VARCHAR(255)
);