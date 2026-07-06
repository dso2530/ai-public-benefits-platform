package com.govtech.connectors.dgfip.dto;

import java.math.BigDecimal;

public record DgfipTaxNoticeResponse(
    String referenceNumber, String fiscalYear, BigDecimal taxableIncome, BigDecimal taxAmount) {}
