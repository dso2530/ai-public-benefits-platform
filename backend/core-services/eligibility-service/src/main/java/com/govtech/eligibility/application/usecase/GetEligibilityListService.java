package com.govtech.eligibility.application.usecase;

import com.govtech.eligibility.application.dto.EligibilityDto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetEligibilityListService {

  public List<EligibilityDto> execute() {

    return List.of(
        new EligibilityDto("APL", "Aide au logement", BigDecimal.valueOf(280)),
        new EligibilityDto("RSA", "Revenu de solidarité active", BigDecimal.valueOf(607)));
  }
}
