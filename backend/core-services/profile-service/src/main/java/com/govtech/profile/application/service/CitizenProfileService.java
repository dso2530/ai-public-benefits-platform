package com.govtech.profile.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.exception.CitizenNotFoundException;
import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.domain.repository.CitizenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitizenProfileService {

    private final CitizenRepository repository;

    @Transactional
    public Citizen update(
            String subject,
            UpdateProfileCommand command) {

        log.debug(
                "Looking up citizen subject={}",
                subject);

        Citizen citizen = repository.findBySubject(subject)
                .orElseThrow(() -> {
                    log.warn(
                            "Citizen not found subject={}",
                            subject);

                    return new CitizenNotFoundException(subject);
                });

        log.debug(
                "Updating citizen profile subject={}",
                subject);

        citizen.update(command);

        Citizen saved = repository.save(citizen);

        log.info(
                "Citizen profile updated and saved subject={}",
                subject);

        return saved;
    }
}