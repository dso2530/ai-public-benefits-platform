package com.govtech.application.service.usecase;

import org.springframework.stereotype.Service;

import com.govtech.application.domain.model.Application;
import com.govtech.application.service.mapper.ApplicationEventMapper;
import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationEventService {

    private final EventPublisher publisher;
    private final ApplicationEventMapper mapper;

    public void publish(Application application) {
        publisher.publish(
                "application.generated",
                application.subject(),
                mapper.toEvent(application));
    }

    
}