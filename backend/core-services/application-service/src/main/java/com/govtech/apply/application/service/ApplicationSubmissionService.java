package com.govtech.apply.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.apply.application.event.ApplicationEventService;
import com.govtech.apply.domain.model.Application;
import com.govtech.apply.domain.model.ApplicationStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationSubmissionService {

    private final ApplicationService applicationService;
    private final ApplicationEventService eventService;

    @Transactional
    public void submit(
            UUID applicationId,
            String subject) {

        Application application = applicationService.findDomain(
                applicationId,
                subject);

        if (application.status() != ApplicationStatus.READY_TO_SUBMIT) {

            throw new IllegalStateException(
                    "Application is not ready to submit");
        }

        Application submitted = application.withStatus(
                ApplicationStatus.SUBMITTED);

        Application saved = applicationService.save(
                submitted);

        eventService.publish(saved);
    }
}