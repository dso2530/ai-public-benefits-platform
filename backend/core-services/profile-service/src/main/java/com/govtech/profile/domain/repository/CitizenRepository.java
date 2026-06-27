package com.govtech.profile.domain.repository;

import com.govtech.profile.domain.model.Citizen;
import java.util.Optional;

public interface CitizenRepository {

  Optional<Citizen> findBySubject(String subject);

  Citizen save(Citizen citizen);
}
