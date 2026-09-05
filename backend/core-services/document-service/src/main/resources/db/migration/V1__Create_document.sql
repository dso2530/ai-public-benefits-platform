-- V1__create_documents.sql

CREATE TABLE documents
(
    id BIGSERIAL PRIMARY KEY,

    subject VARCHAR(255) NOT NULL,

    name VARCHAR(255) NOT NULL,

    status VARCHAR(50) NOT NULL,

    document_type VARCHAR(50) NOT NULL,

    origin VARCHAR(50) NOT NULL,

    source VARCHAR(100) NOT NULL,

    file_name VARCHAR(255),

    sha256 VARCHAR(64),

    content_type VARCHAR(100),

    bucket VARCHAR(100) NOT NULL,

    object_key VARCHAR(500) NOT NULL,

    file_size BIGINT,

    uploaded_at TIMESTAMP WITH TIME ZONE NOT NULL,

    application_id UUID,

    security_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    scanned_at TIMESTAMP WITH TIME ZONE,

    scan_engine VARCHAR(50),

    detected_content_type VARCHAR(100),

    connector_name VARCHAR(100),

    connector_type VARCHAR(50),

    territory_code VARCHAR(50),

    metadata JSONB
);