CREATE TABLE eligibilities (
    id UUID PRIMARY KEY,
    calculation_id UUID NOT NULL,
    calculated_at TIMESTAMP NOT NULL,
    subject VARCHAR(255) NOT NULL,
    aid_code VARCHAR(50) NOT NULL,
    aid_name VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL,
    reason TEXT,
    estimated_amount VARCHAR(50)
);