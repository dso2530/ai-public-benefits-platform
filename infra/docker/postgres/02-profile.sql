\connect profiledb;

CREATE TABLE profiles (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255),
    city VARCHAR(100)
);

INSERT INTO profiles (
    subject,
    first_name,
    last_name,
    email,
    city
)
VALUES (
    'aed0ddab-a7c3-4691-a473-d6d3af16d93b',
    'John',
    'Citizen',
    'citizen@test.fr',
    'Lille'
);