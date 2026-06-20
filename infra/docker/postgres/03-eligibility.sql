\connect eligibilitydb;

CREATE TABLE eligibilities (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    code VARCHAR(50),
    name VARCHAR(255),
    amount NUMERIC(10,2)
);

INSERT INTO eligibilities (
    subject,
    code,
    name,
    amount
)
VALUES (
    'aed0ddab-a7c3-4691-a473-d6d3af16d93b',
    'APL',
    'Aide au logement',
    280
);