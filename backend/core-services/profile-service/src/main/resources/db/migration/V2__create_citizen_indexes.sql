CREATE UNIQUE INDEX idx_citizens_subject
    ON citizens(subject);

CREATE UNIQUE INDEX idx_citizens_email
    ON citizens(email);

CREATE INDEX idx_citizens_tax_number
    ON citizens(tax_number);

CREATE INDEX idx_citizens_city
    ON citizens(city);

CREATE INDEX idx_citizens_postal_code
    ON citizens(postal_code);

CREATE INDEX idx_citizens_last_name
    ON citizens(last_name);