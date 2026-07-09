package com.govtech.profile.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.events.profile.ProfileUpdatedEvent;
import com.govtech.platform.messaging.publisher.EventPublisher;
import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.application.mapper.ProfileUpdatedEventMapper;
import com.govtech.profile.domain.exception.CitizenNotFoundException;
import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.domain.repository.CitizenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateProfileService implements UpdateProfileTaxUseCase {

    private final CitizenRepository repository;

    private final EventPublisher publisher;

    @Transactional
    @Override
    public void updateProfileTaxUseCase(String subject, UpdateProfileCommand updateProfileCommand) {

        Citizen citizen = repository.findBySubject(subject)
                .orElseThrow(() -> new CitizenNotFoundException(subject));

        citizen.update(updateProfileCommand);

        repository.save(citizen);

        ProfileUpdatedEvent event = ProfileUpdatedEventMapper.toEvent(citizen);

        publisher.publish(
                "profile.updated",
                subject,
                event);

    }
}