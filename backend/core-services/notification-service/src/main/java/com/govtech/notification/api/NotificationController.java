package com.govtech.notification.api;

import com.govtech.notification.application.dto.NotificationDto;
import com.govtech.notification.application.dto.NotificationSummaryDto;
import com.govtech.notification.application.usecase.NotificationService;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public List<NotificationDto> getNotifications(@AuthenticationPrincipal Jwt jwt) {

    return notificationService.getNotifications(jwt.getSubject());
  }

  @GetMapping("/summary")
  public NotificationSummaryDto getSummary(@AuthenticationPrincipal Jwt jwt) {

    return notificationService.getSummary(jwt.getSubject());
  }

  @PatchMapping("/{id}/read")
  public void markAsRead(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
    notificationService.markAsRead(id, jwt.getSubject());
  }
}
