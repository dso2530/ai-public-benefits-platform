package com.govtech.profile.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.event.ProfileEventService;
import com.govtech.profile.application.service.CitizenProfileService;
import com.govtech.profile.domain.model.Citizen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateProfileUsecase
                implements UpdateProfileTaxUseCase,
                UpdateSupportingDocumentProfileUseCase {

        private final CitizenProfileService profileService;
        private final ProfileEventService eventService;

        @Override
        @Transactional
        public void updateProfileTax(
                        String subject,
                        UpdateProfileCommand command,
                        EventContext eventContext) {

                log.info(
                                "Updating tax profile - subject={}, correlationId={}",
                                subject,
                                eventContext != null ? eventContext.correlationId() : null);

                Citizen citizen = profileService.update(
                                subject,
                                command);

                log.debug(
                                "Tax profile updated successfully - subject={}, citizenId={}",
                                subject,
                                citizen.getId());

                log.debug(
                                "Publishing ProfileUpdated event - subject={}, citizenId={}, correlationId={}",
                                subject,
                                citizen.getId(),
                                eventContext != null ? eventContext.correlationId() : null);

                eventService.publishProfileUpdated(
                                citizen,
                                eventContext);

                log.info(
                                "Tax profile update completed - subject={}, citizenId={}",
                                subject,
                                citizen.getId());
        }

        @Override
        @Transactional
        public void updateSupportingDocumentProfile(
                        String subject,
                        UpdateProfileCommand command,
                        DocumentCommand documentCommand,
                        EventContext eventContext) {

                log.info(
                                "Updating supporting document profile - subject={}, correlationId={}",
                                subject,
                                eventContext != null ? eventContext.correlationId() : null);

                Citizen citizen = profileService.update(
                                subject,
                                command);

                log.debug(
                                "Supporting document profile updated successfully - subject={}, citizenId={}",
                                subject,
                                citizen.getId());

                log.debug(
                                "Publishing SupportingDocumentUpdated event - subject={}, citizenId={}, correlationId={}",
                                subject,
                                citizen.getId(),
                                eventContext != null ? eventContext.correlationId() : null);

                eventService.publishSupportingDocumentUpdated(
                                citizen,
                                documentCommand,
                                eventContext);

                log.info(
                                "Supporting document profile update completed - subject={}, citizenId={}",
                                subject,
                                citizen.getId());
        }
}