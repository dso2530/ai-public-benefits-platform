package com.govtech.eligibility.application.usecase;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.govtech.eligibility.api.dto.EligibilitySummaryDto;
import com.govtech.eligibility.domain.model.EligibilityStatus;
import com.govtech.eligibility.domain.repository.EligibilityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetEligibilitySummaryService {

  private final EligibilityRepository eligibilityRepository;

  public EligibilitySummaryDto execute(String subject) {

    var eligibilities = eligibilityRepository.findLatestBySubject(subject);

    long eligibleCount = eligibilities.stream()
        .filter(e -> e.status() == EligibilityStatus.ELIGIBLE)
        .count();

    BigDecimal totalAmount = eligibilities.stream()
        .filter(e -> e.status() == EligibilityStatus.ELIGIBLE)
        .map(e -> e.estimatedAmount())
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return EligibilitySummaryDto.builder()
        .eligibleCount(eligibleCount)
        .notEligibleCount(eligibilities.size() - eligibleCount)
        .totalAmount(totalAmount).build();

  }

}