package com.govtech.citizen.notification.dto;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto(
    UUID id, String title, String message, boolean read, Instant createdAt) {}
