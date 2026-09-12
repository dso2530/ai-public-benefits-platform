package com.govtech.eligibility.infrastructure.persistence;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.govtech.eligibility.domain.model.Eligibility;
import com.govtech.eligibility.domain.repository.EligibilityRepository;
import com.govtech.eligibility.infrastructure.persistence.entity.EligibilityJpaEntity;
import com.govtech.eligibility.infrastructure.persistence.mapper.EligibilityJpaMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EligibilityRepositoryAdapter
        implements EligibilityRepository {

    private final EligibilityJpaRepository repository;

    @Override
    public void saveAll(List<Eligibility> eligibilities) {

        repository.saveAll(
                eligibilities.stream()
                        .map(EligibilityJpaMapper::toEntity)
                        .toList());
    }

    @Override
    public List<Eligibility> findLatestBySubject(String subject) {

        List<EligibilityJpaEntity> entities = repository.findBySubjectOrderByCalculatedAtDesc(subject);

        if (entities.isEmpty()) {
            return List.of();
        }

        UUID latestCalculationId = entities.getFirst().getCalculationId();

        return repository.findByCalculationId(latestCalculationId)
                .stream()
                .collect(Collectors.toMap(
                        EligibilityJpaEntity::getAidCode,
                        Function.identity(),
                        (first, second) -> first,
                        LinkedHashMap::new))
                .values()
                .stream()
                .map(EligibilityJpaMapper::toDomain)
                .toList();
    }
}