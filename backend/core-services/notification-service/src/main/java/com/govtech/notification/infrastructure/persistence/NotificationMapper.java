package com.govtech.notification.infrastructure.persistence;

import com.govtech.notification.domain.model.Notification;
import com.govtech.notification.domain.model.NotificationId;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

  public Notification toDomain(NotificationJpaEntity entity) {

    return new Notification(
        new NotificationId(entity.getId()),
        entity.getSubject(),
        entity.getTitle(),
        entity.getMessage(),
        entity.isRead(),
        entity.getCreatedAt());
  }

  public NotificationJpaEntity toEntity(Notification notification) {

    NotificationJpaEntity entity = new NotificationJpaEntity();

    if (notification.getId() != null) {
      entity.setId(notification.getId().value());
    }

    entity.setSubject(notification.getSubject());
    entity.setTitle(notification.getTitle());
    entity.setMessage(notification.getMessage());
    entity.setRead(notification.isRead());
    entity.setCreatedAt(notification.getCreatedAt());

    return entity;
  }
}
