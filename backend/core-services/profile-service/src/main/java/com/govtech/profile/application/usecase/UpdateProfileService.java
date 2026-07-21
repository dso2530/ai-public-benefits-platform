package com.govtech.profile.application.usecase;

import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.platform.messaging.publisher.EventPublisher;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.ProfileUpdatedEventMapper;
import com.govtech.profile.application.mapper.SupportingDocumentUpdatedEventMapper;
import com.govtech.profile.domain.exception.CitizenNotFoundException;
import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.domain.repository.CitizenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateProfileService
                implements UpdateProfileTaxUseCase,
                UpdateSupportingDocumentProfileUseCase {

        private final CitizenRepository repository;
        private final EventPublisher publisher;

        @Override
        @Transactional
        public void updateProfileTaxUseCase(
                        String subject,
                        UpdateProfileCommand command) {

                updateProfile(
                                subject,
                                command,
                                "profile.updated",
                                ProfileUpdatedEventMapper::toEvent);
        }

        @Override
        @Transactional
        public void updateSupportingDocumentProfile(
                        String subject,
                        UpdateProfileCommand command,
                        DocumentBaseEvent metadata) {

                updateProfile(
                                subject,
                                command,
                                "supporting.document.updated",
                                citizen -> SupportingDocumentUpdatedEventMapper.toEvent(metadata));
        }

        private <T> void updateProfile(
                        String subject,
                        UpdateProfileCommand command,
                        String topic,
                        Function<Citizen, T> eventMapper) {

                Citizen citizen = repository.findBySubject(subject)
                                .orElseThrow(() -> new CitizenNotFoundException(subject));

                citizen.update(command);

                citizen = repository.save(citizen);

                publisher.publish(
                                topic,
                                subject,
                                eventMapper.apply(citizen));
        }
}