package com.govtech.notification.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.notification.application.mapper.NotificationEventMapper;
import com.govtech.notification.domain.model.Notification;
import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationEventService {

    private final EventPublisher eventPublisher;
    private final NotificationEventMapper notificationMapper;

    public void publish(Notification notification) {
        eventPublisher.publish(
                "notification.created",
                notification.getSubject(),
                notificationMapper.toEvent(notification));
    }

    public void publish(List<Notification> notifications) {
        notifications.forEach(this::publish);
    }
}