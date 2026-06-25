package com.govtech.eligibility.application.usecase;

import com.govtech.eligibility.application.dto.EligibilitySummaryDto;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class GetEligibilitySummaryService {

  public EligibilitySummaryDto execute() {

    return new EligibilitySummaryDto(7, 2, BigDecimal.valueOf(4280));
  }
}
