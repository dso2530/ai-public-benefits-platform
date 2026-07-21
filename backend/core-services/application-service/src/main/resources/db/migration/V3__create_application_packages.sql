CREATE TABLE application_packages (
    id UUID PRIMARY KEY,
    application_id UUID NOT NULL UNIQUE,
    object_key VARCHAR(500) NOT NULL,
    status VARCHAR(50) NOT NULL,
    generated_at TIMESTAMP NOT NULL
);