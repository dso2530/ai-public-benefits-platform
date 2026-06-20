
package  com.govtech.notification.api;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.notification.application.dto.NotificationDto;
import com.govtech.notification.application.dto.NotificationSummaryDto;
import com.govtech.notification.application.usecase.NotificationService;

import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

   private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDto> getNotifications(
            @AuthenticationPrincipal Jwt jwt) {

        return notificationService.getNotifications(
                jwt.getSubject());
    }

    @GetMapping("/summary")
    public NotificationSummaryDto getSummary(
            @AuthenticationPrincipal Jwt jwt) {

        return notificationService.getSummary(
                jwt.getSubject());
    }
}