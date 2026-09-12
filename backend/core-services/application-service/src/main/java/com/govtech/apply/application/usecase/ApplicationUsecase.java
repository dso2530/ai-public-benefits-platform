package com.govtech.apply.application.usecase;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.apply.api.dto.ApplicationDto;
import com.govtech.apply.application.event.ApplicationEventService;
import com.govtech.apply.application.service.ApplicationPackageService;
import com.govtech.apply.application.service.ApplicationQueryService;
import com.govtech.apply.application.service.ApplicationService;
import com.govtech.apply.application.service.ApplicationSubmissionService;
import com.govtech.apply.domain.model.Application;
import com.govtech.platform.messaging.event.EventContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationUsecase {

        private final ApplicationService applicationService;
        private final ApplicationPackageService packageService;
        private final ApplicationSubmissionService submissionService;
        private final ApplicationQueryService queryService;
        private final ApplicationEventService eventService;

        @Transactional
        public Application createFromEligibility(Application application, EventContext eventContext) {

                Application created = applicationService.create(application);

                packageService.generatePackage(
                                created.applicationId(), created.subject());

                eventService.publish(application, eventContext);

                return applicationService.findDomain(
                                created.applicationId(),
                                created.subject());
        }

        @Transactional
        public void refreshApplication(
                        UUID applicationId,
                        String subject) {

                packageService.refresh(
                                applicationId,
                                subject);
        }

        @Transactional
        public void submit(
                        UUID applicationId,
                        String subject) {

                submissionService.submit(
                                applicationId,
                                subject);
        }

        @Transactional(readOnly = true)
        public Optional<ApplicationDto> findById(
                        UUID applicationId,
                        String subject) {

                return queryService.findById(
                                applicationId,
                                subject);
        }

        @Transactional(readOnly = true)
        public java.util.List<ApplicationDto> findBySubject(
                        String subject) {

                return queryService.findBySubject(subject);
        }

        @Transactional(readOnly = true)
        public com.govtech.platform.storage.dto.DownloadedDocument downloadPackage(
                        UUID applicationId,
                        String subject)
                        throws java.nio.file.AccessDeniedException {

                return queryService.downloadPackage(
                                applicationId,
                                subject);
        }
}