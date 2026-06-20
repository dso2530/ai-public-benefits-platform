package com.govtech.notification.domain.model;

import java.time.Instant;

public class Notification {

    private NotificationId id;
    private String subject;
    private String title;
    private String message;
    private boolean read;
    private Instant createdAt;

    public Notification(
            NotificationId id,
            String subject,
            String title,
            String message,
            boolean read,
            Instant createdAt) {

        this.id = id;
        this.subject = subject;
        this.title = title;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
    }

    public NotificationId getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}