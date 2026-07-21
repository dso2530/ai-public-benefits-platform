package com.govtech.notification.application.mapper;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.events.common.BaseEvent;
import com.govtech.events.eligibility.EligibilityCheckedEvent;
import com.govtech.events.eligibility.EligibilityResult;
import com.govtech.events.notification.NotificationCreatedEvent;
import com.govtech.notification.application.configuration.NotificationProperties;
import com.govtech.notification.domain.model.Notification;
import com.govtech.notification.domain.model.NotificationId;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventMapper {

        private final NotificationProperties notificationProperties;

        public List<Notification> toDomain(EligibilityCheckedEvent event) {

                return event.getEligibilities().stream()
                                .filter(EligibilityResult::getEligible)
                                .map(e -> {
                                        NotificationProperties.Template template = notificationProperties.getTemplates()
                                                        .get(e.getAidCode());

                                        String message = template.getMessage()
                                                        .replace(
                                                                        "{estimatedAmountLabel}",
                                                                        Objects.toString(e.getEstimatedAmount(),
                                                                                        ""));

                                        return Notification.builder()
                                                        .id(new NotificationId(UUID.randomUUID()))
                                                        .subject(event.getMetadata().getSubject())
                                                        .title(template.getTitle())
                                                        .message(message)
                                                        .read(false)
                                                        .createdAt(Instant.parse(event.getMetadata().getOccurredAt()))
                                                        .build();
                                })
                                .toList();
        }

        public NotificationCreatedEvent toEvent(Notification notification) {

                BaseEvent metadata = BaseEvent.newBuilder()
                                .setEventId(notification.getId().value().toString())
                                .setOccurredAt(notification.getCreatedAt().atOffset(ZoneOffset.UTC).toString())
                                .setSubject(notification.getSubject())
                                .build();

                return NotificationCreatedEvent.newBuilder()
                                .setMetadata(metadata)
                                .setTitle(notification.getTitle())
                                .setMessage(notification.getMessage())
                                .build();
        }
}
