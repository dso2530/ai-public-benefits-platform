package com.govtech.shared.model;

import java.math.BigDecimal;

public record TaxNotice(
    String referenceNumber, String fiscalYear, BigDecimal taxableIncome, BigDecimal taxAmount) {}
