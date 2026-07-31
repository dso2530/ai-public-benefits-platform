CREATE TABLE consent_audit
(
    id UUID PRIMARY KEY,

    consent_id UUID NOT NULL,

    action VARCHAR(50) NOT NULL,

    actor VARCHAR(100),

    ip_address VARCHAR(45),

    user_agent VARCHAR(255),

    created_at TIMESTAMP WITH TIME ZONE
);


CREATE INDEX idx_consent_audit_consent
ON consent_audit(consent_id);