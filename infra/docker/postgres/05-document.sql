\connect documentdb;

CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    type VARCHAR(100),
    status VARCHAR(50),
    uploaded_at TIMESTAMP
);

INSERT INTO documents (
    subject,
    name,
    type,
    status,
    uploaded_at
)
VALUES (
    'aed0ddab-a7c3-4691-a473-d6d3af16d93b',
    'avis-imposition.pdf',
    'TAX',
    'VALIDATED',
    now()
);
INSERT INTO documents (
    subject,
    name,
    type,
    status,
    uploaded_at
)
VALUES (
    'aed0ddab-a7c3-4691-a473-d6d3af16d93b',
    'quittance-loyer.pdf',
    'HOUSING',
    'PENDING',
    now()
);