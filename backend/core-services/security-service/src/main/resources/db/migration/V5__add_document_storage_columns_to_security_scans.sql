ALTER TABLE security_scans
    ADD COLUMN bucket VARCHAR(255),
    ADD COLUMN object_key VARCHAR(1000),
    ADD COLUMN content_type VARCHAR(100),
    ADD COLUMN file_name VARCHAR(500),
    ADD COLUMN document_type VARCHAR(50);