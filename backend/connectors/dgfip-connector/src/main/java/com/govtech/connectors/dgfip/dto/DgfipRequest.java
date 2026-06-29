package com.govtech.connectors.dgfip.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DgfipRequest {

    private String fiscalNumber;

}