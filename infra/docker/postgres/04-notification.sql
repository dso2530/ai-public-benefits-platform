\connect notificationdb;

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    title VARCHAR(255),
    message TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP
);

INSERT INTO notifications (
    subject,
    title,
    message,
    is_read,
    created_at
)
VALUES (
    'aed0ddab-a7c3-4691-a473-d6d3af16d93b',
    'Demande validée',
    'Votre demande APL a été validée',
    false,
    now()
);