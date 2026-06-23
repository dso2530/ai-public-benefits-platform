\connect documentdb;

CREATE INDEX idx_documents_subject
ON documents(subject);