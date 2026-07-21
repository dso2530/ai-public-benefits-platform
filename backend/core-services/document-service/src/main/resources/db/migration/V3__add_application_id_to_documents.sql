ALTER TABLE documents
ADD COLUMN application_id UUID;

CREATE INDEX idx_documents_application_id
ON documents(application_id);