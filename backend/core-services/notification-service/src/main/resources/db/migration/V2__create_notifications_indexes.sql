CREATE INDEX idx_notifications_subject
    ON notifications(subject);

CREATE INDEX idx_notifications_created_at
    ON notifications(created_at DESC);

CREATE INDEX idx_notifications_subject_is_read
    ON notifications(subject, is_read); 