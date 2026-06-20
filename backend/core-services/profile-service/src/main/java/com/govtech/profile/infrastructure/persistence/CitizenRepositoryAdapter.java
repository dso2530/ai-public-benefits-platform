package com.govtech.profile.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.domain.repository.CitizenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CitizenRepositoryAdapter
        implements CitizenRepository {

    private final CitizenJpaRepository repository;

    @Override
    public Optional<Citizen> findBySubject(
            String subject) {

        return repository
                .findBySubject(subject)
                .map(CitizenJpaMapper::toDomain);
    }

    @Override
    public Citizen save(Citizen citizen) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}