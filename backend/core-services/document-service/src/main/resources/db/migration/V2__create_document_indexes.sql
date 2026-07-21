CREATE INDEX idx_documents_subject
    ON documents(subject);

CREATE INDEX idx_documents_status
    ON documents(status);

CREATE INDEX idx_documents_uploaded_at
    ON documents(uploaded_at DESC);

CREATE INDEX idx_documents_subject_uploaded_at
    ON documents(subject, uploaded_at DESC);