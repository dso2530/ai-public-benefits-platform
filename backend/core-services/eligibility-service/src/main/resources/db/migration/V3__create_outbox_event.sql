CREATE TABLE outbox_event (
    id UUID PRIMARY KEY,

    event_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    topic VARCHAR(255) NOT NULL,

    aggregate_type VARCHAR(255),
    aggregate_id VARCHAR(255),

    payload BYTEA NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    claimed_at TIMESTAMP WITH TIME ZONE,
    published_at TIMESTAMP WITH TIME ZONE,

    attempts INTEGER NOT NULL DEFAULT 0,
    last_error TEXT,

    CONSTRAINT uk_outbox_event_event_id
        UNIQUE (event_id)
);

CREATE INDEX idx_outbox_event_status_created_at
    ON outbox_event (status, created_at);

CREATE INDEX idx_outbox_event_status_claimed_at
    ON outbox_event (status, claimed_at);