package com.govtech.notification.infrastructure.kafka;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.eligibility.EligibilityCheckedEvent;
import com.govtech.notification.application.mapper.NotificationEventMapper;
import com.govtech.notification.application.usecase.NotificationService;
import com.govtech.notification.domain.model.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EligibilityCheckedListener {

    private final NotificationEventMapper eventMapper;

    private final NotificationService notificationService;

    @KafkaListener(topics = "eligibility.checked", groupId = "${messaging.kafka.group-id}")
    public void consume(EligibilityCheckedEvent eligibilityCheckedEvent) {

        log.info(
                "Notification received for subject {}",
                eligibilityCheckedEvent.getMetadata().getSubject());

        List<Notification> notifications = eventMapper.toDomain(eligibilityCheckedEvent);

        notificationService.create(notifications);
    }
}