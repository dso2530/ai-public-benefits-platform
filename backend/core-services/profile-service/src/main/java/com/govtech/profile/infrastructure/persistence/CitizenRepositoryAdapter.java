package com.govtech.profile.infrastructure.persistence;

import com.govtech.profile.domain.model.Citizen;
import com.govtech.profile.domain.repository.CitizenRepository;
import com.govtech.profile.infrastructure.persistence.entity.CitizenJpaEntity;
import com.govtech.profile.infrastructure.persistence.mapper.CitizenJpaMapper;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CitizenRepositoryAdapter implements CitizenRepository {

  private final CitizenJpaRepository repository;

  @Override
  public Optional<Citizen> findBySubject(String subject) {

    return repository.findBySubject(subject).map(CitizenJpaMapper::toDomain);
  }

  @Override
  public Citizen save(Citizen citizen) {
    CitizenJpaEntity entity = CitizenJpaMapper.toEntity(citizen);
    CitizenJpaEntity saved = repository.save(entity);
    return CitizenJpaMapper.toDomain(saved);
  }
}
