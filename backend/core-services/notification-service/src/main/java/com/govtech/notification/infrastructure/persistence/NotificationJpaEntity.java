package com.govtech.notification.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationJpaEntity {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String subject;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 1000)
  private String message;

  @Column(name = "is_read")
  private boolean read;

  @Column(nullable = false)
  private Instant createdAt;

  // getters/setters
}
