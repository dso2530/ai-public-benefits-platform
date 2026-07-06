package com.govtech.connectors.dgfip.dto;

import java.math.BigDecimal;

public record DgfipIncomeResponse(
    String fiscalNumber,
    String fiscalYear,
    BigDecimal taxableIncome,
    BigDecimal annualIncome,
    Integer householdParts) {}
