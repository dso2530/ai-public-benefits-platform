CREATE INDEX idx_security_scans_document_id
ON security_scans(document_id);


CREATE INDEX idx_security_scans_sha256
ON security_scans(sha256);


CREATE INDEX idx_security_scans_status
ON security_scans(status);