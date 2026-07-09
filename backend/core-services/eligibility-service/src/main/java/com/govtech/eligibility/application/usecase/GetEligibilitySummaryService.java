package com.govtech.eligibility.application.usecase;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

import com.govtech.eligibility.api.dto.EligibilitySummaryDto;
import com.govtech.eligibility.domain.model.Eligibility;
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
        .map(this::toBigDecimal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new EligibilitySummaryDto(
        eligibilities.size(),
        (int) eligibleCount,
        totalAmount);
  }

  private BigDecimal toBigDecimal(Eligibility eligibility) {
    try {
      return eligibility.estimatedAmount() == null
          ? BigDecimal.ZERO
          : new BigDecimal(eligibility.estimatedAmount());
    } catch (NumberFormatException e) {
      return BigDecimal.ZERO;
    }
  }
}