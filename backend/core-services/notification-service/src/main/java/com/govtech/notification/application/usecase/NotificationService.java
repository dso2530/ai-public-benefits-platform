package com.govtech.notification.application.usecase;

import com.govtech.notification.application.dto.NotificationDto;
import com.govtech.notification.application.dto.NotificationSummaryDto;
import com.govtech.notification.domain.model.Notification;
import com.govtech.notification.infrastructure.persistence.NotificationJpaEntity;
import com.govtech.notification.infrastructure.persistence.NotificationJpaRepository;
import com.govtech.notification.infrastructure.persistence.NotificationMapper;

import jakarta.persistence.EntityNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

  private final NotificationJpaRepository repository;
  private final NotificationMapper mapper;

  public List<NotificationDto> getNotifications(String subject) {

    return repository.findBySubjectOrderByCreatedAtDesc(subject).stream()
        .map(mapper::toDomain)
        .map(this::toDto)
        .toList();
  }

  public void create(List<Notification> notifications) {

    List<NotificationJpaEntity> entities = notifications.stream()
        .map(mapper::toEntity)
        .toList();

    repository.saveAll(entities);
  }

  public NotificationSummaryDto getSummary(String subject) {

    long total = repository.countBySubject(subject);
    long unread = repository.countBySubjectAndReadFalse(subject);

    return new NotificationSummaryDto((int) total, (int) unread);
  }

  @Transactional
  public void markAsRead(UUID id, String subject) {

    NotificationJpaEntity notification = repository.findByIdAndSubject(id, subject)
        .orElseThrow(() -> new EntityNotFoundException(
            "Notification " + id + " introuvable"));

    notification.setRead(Boolean.TRUE);

    repository.save(notification);
  }

  private NotificationDto toDto(Notification notification) {

    return new NotificationDto(
        notification.getId().value(),
        notification.getTitle(),
        notification.getMessage(),
        notification.isRead(),
        notification.getCreatedAt());
  }
}
