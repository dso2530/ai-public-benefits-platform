package  com.govtech.notification.application.usecase;
import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.notification.application.dto.NotificationDto;
import com.govtech.notification.application.dto.NotificationSummaryDto;
import com.govtech.notification.domain.model.Notification;
import com.govtech.notification.infrastructure.persistence.NotificationJpaRepository;
import com.govtech.notification.infrastructure.persistence.NotificationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    
    private final NotificationJpaRepository repository;
    private final NotificationMapper mapper;

    public List<NotificationDto> getNotifications(String subject) {

        return repository
                .findBySubjectOrderByCreatedAtDesc(subject)
                .stream()
                .map(mapper::toDomain)
                .map(this::toDto)
                .toList();
    }

    public NotificationSummaryDto getSummary(String subject) {

        long total = repository.countBySubject(subject);
        long unread = repository.countBySubjectAndReadFalse(subject);

        return new NotificationSummaryDto(
                (int) total,
                (int) unread
        );
    }

    private NotificationDto toDto(Notification notification) {

        return new NotificationDto(
                notification.getId().value(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}