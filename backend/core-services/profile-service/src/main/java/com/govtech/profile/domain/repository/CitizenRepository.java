package com.govtech.profile.domain.repository;

import java.util.Optional;

import com.govtech.profile.domain.model.Citizen;

public interface CitizenRepository {

    Optional<Citizen> findBySubject(
            String subject);

    Citizen save(
            Citizen citizen);
}