package com.govtech.shared.model;

import java.math.BigDecimal;

public record Income(BigDecimal annualIncome, BigDecimal taxableIncome, String fiscalYear) {}
