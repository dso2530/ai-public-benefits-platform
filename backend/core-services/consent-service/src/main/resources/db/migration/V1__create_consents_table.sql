CREATE TABLE consents
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    purpose VARCHAR(100) NOT NULL,

    version VARCHAR(20) NOT NULL,

    status VARCHAR(20) NOT NULL,

    source VARCHAR(50),

    granted_at TIMESTAMP WITH TIME ZONE NOT NULL,

    revoked_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITH TIME ZONE
);


CREATE INDEX idx_consents_user_id
    ON consents(user_id);


CREATE INDEX idx_consents_user_purpose
    ON consents(user_id, purpose);


CREATE INDEX idx_consents_status
    ON consents(status);