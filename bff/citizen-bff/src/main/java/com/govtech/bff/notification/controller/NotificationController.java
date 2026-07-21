package com.govtech.bff.notification.controller;

import com.govtech.bff.clients.NotificationClient;
import com.govtech.bff.notification.dto.NotificationDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationClient notificationClient;

  @GetMapping
  public List<NotificationDto> getNotifications() {
    return notificationClient.getNotifications();
  }

  @PatchMapping("/{id}/read")
  public void markAsRead(@PathVariable UUID id) {
    notificationClient.markAsRead(id);
  }
}
