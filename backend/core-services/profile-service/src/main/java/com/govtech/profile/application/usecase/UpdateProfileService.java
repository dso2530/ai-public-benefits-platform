package com.govtech.profile.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.profile.application.dto.UpdateProfileCommand;
import com.govtech.profile.domain.exception.CitizenNotFoundException;
import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.domain.repository.CitizenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateProfileService implements UpdateProfileTaxUseCase {

    private final CitizenRepository repository;

    @Transactional
    @Override
    public void update(String subject, UpdateProfileCommand updateProfileCommand) {

        Citizen citizen = repository.findBySubject(subject)
                .orElseThrow(() -> new CitizenNotFoundException(subject));

        citizen.update(updateProfileCommand);

        repository.save(citizen);
    }
}