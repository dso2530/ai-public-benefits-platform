CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE citizens (
    id UUID PRIMARY KEY,

    subject VARCHAR(255) NOT NULL,

    email VARCHAR(255) NOT NULL,

    first_name VARCHAR(255),
    last_name VARCHAR(255),

    birth_date DATE,
    birth_place VARCHAR(255),
    nationality VARCHAR(255),

    street VARCHAR(255),
    postal_code VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(255),

    housing_status VARCHAR(255),

    children_count INTEGER,
    single_parent BOOLEAN,

    iban VARCHAR(255),
    bic VARCHAR(255),

    tax_number VARCHAR(255),
    tax_year INTEGER,
    assessment_year INTEGER,

    reference_income VARCHAR(255),
    number_of_shares NUMERIC(38,2),
    income_tax VARCHAR(255),
    refund_amount VARCHAR(255),
    amount_to_pay VARCHAR(255),
    refund_date DATE
);