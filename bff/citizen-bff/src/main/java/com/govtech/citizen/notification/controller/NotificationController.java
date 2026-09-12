package com.govtech.citizen.notification.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.citizen.clients.NotificationClient;
import com.govtech.citizen.notification.dto.NotificationDto;

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
