CREATE TABLE security_scans
(
    id UUID PRIMARY KEY,

    document_id BIGINT NOT NULL,

    sha256 VARCHAR(64),

    status VARCHAR(30) NOT NULL,

    scan_engine VARCHAR(50),

    scanned_at TIMESTAMP WITH TIME ZONE NOT NULL
);